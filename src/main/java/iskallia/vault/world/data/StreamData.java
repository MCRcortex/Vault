package iskallia.vault.world.data;

import iskallia.vault.Vault;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.HypeBarMessage;
import iskallia.vault.util.NetcodeUtils;
import iskallia.vault.world.raid.ArenaRaid;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamData extends WorldSavedData {

    protected static final String DATA_NAME = Vault.MOD_ID + "_StreamSubs";

    private Map<UUID, Subscribers> subBufferMap = new HashMap<>();
    private Map<UUID, Subscribers> subMap = new HashMap<>();
    private Map<UUID, Donations> donoMap = new HashMap<>();

    public StreamData() {
        this(DATA_NAME);
    }

    public StreamData(String name) {
        super(name);
    }

    public StreamData onSub(MinecraftServer server, UUID streamer, String subscriber) {
        NetcodeUtils.runIfPresent(server, streamer, player -> {
            ArenaRaid activeRaid = ArenaRaidData.get(player.getServerWorld()).getActiveFor(player);

            if (activeRaid != null) {
                // Just put incoming sub to the buffer, if an arena is already in progress
                Subscribers subscribers = subBufferMap.computeIfAbsent(streamer, uuid -> new Subscribers());
                subscribers.onSub(subscriber);

            } else {
                Subscribers subscribers = subMap.computeIfAbsent(streamer, uuid -> new Subscribers());
                subscribers.onSub(subscriber);
                syncHypebar(server, streamer);
                int maxSubs = ModConfigs.STREAMER_MULTIPLIERS.ofStreamer(player.getDisplayName().getString()).subsNeededForArena;
                if (subscribers.count() >= maxSubs) {
                    List<String> fighterSubs = subscribers.subs.subList(0, maxSubs);
                    // TODO: Put last "maxSubs" amount of subs to ArenaSpawner
                    fighterSubs.clear();
                    ArenaRaidData.get(player.getServerWorld()).startNew(player);
                }
            }

            this.markDirty();
        });

        return this;
    }

    public StreamData onDono(MinecraftServer server, UUID streamer, String donator, int amount) {
        this.donoMap.computeIfAbsent(streamer, uuid -> new Donations()).onDono(donator, amount);
        this.markDirty();
        return this;
    }

    // TODO: Call dis bad boi when dimension is changed and changed dimension != ARENA
    public StreamData onArenaLeave(MinecraftServer server, UUID streamer) {
        NetcodeUtils.runIfPresent(server, streamer, player -> {
            Subscribers bufferedSubs = subBufferMap.computeIfAbsent(streamer, uuid -> new Subscribers());
            int maxSubs = ModConfigs.STREAMER_MULTIPLIERS.ofStreamer(player.getDisplayName().getString()).subsNeededForArena;
            int subsToMove = Math.min(bufferedSubs.count(), maxSubs);
            for (int i = 0; i < subsToMove; i++) {
                onSub(server, streamer, bufferedSubs.popOneSub());
            }
            this.markDirty();
        });
        return this;
    }

    @Override
    public void read(CompoundNBT nbt) {
        System.out.println("Reading:" + nbt);

        this.subMap = readMap(nbt, "StreamSubs", ListNBT.class, list -> {
            Subscribers subs = new Subscribers();
            subs.deserializeNBT(list);
            return subs;
        });

        this.subBufferMap = readMap(nbt, "StreamSubsBuffer", ListNBT.class, list -> {
            Subscribers subs = new Subscribers();
            subs.deserializeNBT(list);
            return subs;
        });

        this.donoMap = readMap(nbt, "StreamDonos", CompoundNBT.class, list -> {
            Donations donos = new Donations();
            donos.deserializeNBT(list);
            return donos;
        });
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        writeMap(nbt, "StreamSubsBuffer", this.subBufferMap, ListNBT.class, Subscribers::serializeNBT);
        writeMap(nbt, "StreamSubs", this.subMap, ListNBT.class, Subscribers::serializeNBT);
        writeMap(nbt, "StreamDonos", this.donoMap, CompoundNBT.class, Donations::serializeNBT);
        return nbt;
    }

    public void syncHypebar(MinecraftServer server, UUID streamer) {
        NetcodeUtils.runIfPresent(server, streamer, player -> {
            int currentHype = subMap.computeIfAbsent(streamer, uuid -> new Subscribers()).count();
            int maxHype = ModConfigs.STREAMER_MULTIPLIERS.ofStreamer(player.getDisplayName().getString()).subsNeededForArena;
            ModNetwork.CHANNEL.sendTo(
                    new HypeBarMessage(currentHype, maxHype),
                    player.connection.netManager,
                    NetworkDirection.PLAY_TO_CLIENT
            );
        });
    }

    public static <T, N extends INBT> Map<UUID, T> readMap(CompoundNBT nbt, String name, Class<N> nbtType, Function<N, T> mapper) {
        Map<UUID, T> res = new HashMap<>();
        ListNBT uuidList = nbt.getList(name + "Keys", Constants.NBT.TAG_STRING);
        ListNBT valuesList = (ListNBT) nbt.get(name + "Values");

        if (uuidList.size() != valuesList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }

        for (int i = 0; i < uuidList.size(); i++) {
            res.put(UUID.fromString(uuidList.get(i).getString()), mapper.apply((N) valuesList.get(i)));
        }

        return res;
    }

    public static <T, N extends INBT> void writeMap(CompoundNBT nbt, String name, Map<UUID, T> map, Class<N> nbtType, Function<T, N> mapper) {
        ListNBT uuidList = new ListNBT();
        ListNBT valuesList = new ListNBT();
        map.forEach((key, value) -> {
            uuidList.add(StringNBT.valueOf(key.toString()));
            valuesList.add(mapper.apply(value));
        });
        nbt.put(name + "Keys", uuidList);
        nbt.put(name + "Values", valuesList);
    }

    public static StreamData get(ServerWorld world) {
        return world.getServer().func_241755_D_().getSavedData()
                .getOrCreate(StreamData::new, DATA_NAME);
    }

    public static class Subscribers implements INBTSerializable<ListNBT> {
        private final List<String> subs = new LinkedList<>();

        public void onSub(String subscriber) {
            this.subs.add(subscriber);
        }

        public int count() {
            return subs.size();
        }

        public String popOneSub() {
            return subs.remove(0);
        }

        @Override
        public ListNBT serializeNBT() {
            return this.subs.stream().map(StringNBT::valueOf).collect(Collectors.toCollection(ListNBT::new));
        }

        @Override
        public void deserializeNBT(ListNBT nbt) {
            IntStream.range(0, nbt.size()).mapToObj(nbt::getString).forEach(this.subs::add);
        }
    }

    public static class Donations implements INBTSerializable<CompoundNBT> {
        private final Map<String, Integer> donoMap = new HashMap<>();

        public Donations onDono(String name, int amount) {
            this.donoMap.put(name, this.donoMap.getOrDefault(name, 0) + amount);
            return this;
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            ListNBT donators = new ListNBT();
            ListNBT amounts = new ListNBT();

            this.donoMap.forEach((donator, amount) -> {
                donators.add(StringNBT.valueOf(donator));
                amounts.add(IntNBT.valueOf(amount));
            });

            nbt.put("Donators", donators);
            nbt.put("Amounts", amounts);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            ListNBT donators = nbt.getList("Donators", Constants.NBT.TAG_STRING);
            ListNBT amounts = nbt.getList("Amounts", Constants.NBT.TAG_INT);

            if (donators.size() != amounts.size()) {
                throw new IllegalStateException("Map doesn't have the same amount of keys as values");
            }

            for (int i = 0; i < donators.size(); i++) {
                this.donoMap.put(donators.getString(i), amounts.getInt(i));
            }
        }
    }

}
