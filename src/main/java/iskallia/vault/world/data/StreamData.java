package iskallia.vault.world.data;

import iskallia.vault.Vault;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.HypeBarMessage;
import iskallia.vault.util.NetcodeUtils;
import iskallia.vault.util.nbt.NBTHelper;
import iskallia.vault.world.raid.ArenaRaid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.*;
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

    public Donations getDonations(UUID streamer) {
        return this.donoMap.computeIfAbsent(streamer, uuid -> new Donations());
    }

    public StreamData reset(MinecraftServer server, UUID streamer) {
        this.subMap.put(streamer, new Subscribers());
        this.subBufferMap.put(streamer, new Subscribers());
        this.donoMap.put(streamer, new Donations());

        syncHypebar(server, streamer);

        this.markDirty();
        return this;
    }

    public StreamData resetDonos(MinecraftServer server, UUID streamer) {
        this.donoMap.put(streamer, new Donations());

        this.markDirty();
        return this;
    }

    public StreamData onSub(MinecraftServer server, UUID streamer, String name, int months) {
        NetcodeUtils.runIfPresent(server, streamer, player -> {
            ArenaRaid activeRaid = ArenaRaidData.get(player.getServerWorld()).getActiveFor(player);

            if (activeRaid != null) {
                // Just put incoming sub to the buffer, if an arena is already in progress
                Subscribers subscribers = subBufferMap.computeIfAbsent(streamer, uuid -> new Subscribers());
                subscribers.onSub(name, months);
            } else {
                Subscribers subscribers = subMap.computeIfAbsent(streamer, uuid -> new Subscribers());
                subscribers.onSub(name, months);
                syncHypebar(server, streamer);
                int maxSubs = ModConfigs.STREAMER_MULTIPLIERS.ofStreamer(player.getDisplayName().getString()).subsNeededForArena;
                if (subscribers.count() >= maxSubs) {
                    ArenaRaid raid = ArenaRaidData.get(player.getServerWorld()).startNew(player);
                    List<Subscribers.Instance> fighterSubs = subscribers.subs.subList(0, maxSubs);
                    raid.spawner.addFighters(fighterSubs);
                    fighterSubs.clear();
                }
            }

            this.markDirty();
        });

        return this;
    }

    public StreamData onDono(MinecraftServer server, UUID streamer, String donator, int amount) {
        getDonations(streamer).onDono(donator, amount);
        this.markDirty();
        return this;
    }

    // TODO: Call dis bad boi when dimension is changed and changed dimension != ARENA
    // TODO: Should be complete now ^
    public StreamData onArenaLeave(MinecraftServer server, UUID streamer) {
        NetcodeUtils.runIfPresent(server, streamer, player -> {
            Subscribers bufferedSubs = subBufferMap.computeIfAbsent(streamer, uuid -> new Subscribers());
            int maxSubs = ModConfigs.STREAMER_MULTIPLIERS.ofStreamer(player.getDisplayName().getString()).subsNeededForArena;
            int subsToMove = Math.min(bufferedSubs.count(), maxSubs);
            for (int i = 0; i < subsToMove; i++) {
                Subscribers.Instance e = bufferedSubs.popOneSub();
                onSub(server, streamer, e.name, e.months);
            }
            this.markDirty();
        });

        return this;
    }

    @Override
    public void read(CompoundNBT nbt) {
        System.out.println("Reading:" + nbt);

        this.subMap = NBTHelper.readMap(nbt, "StreamSubs", ListNBT.class, list -> {
            Subscribers subs = new Subscribers();
            subs.deserializeNBT(list);
            return subs;
        });

        this.subBufferMap = NBTHelper.readMap(nbt, "StreamSubsBuffer", ListNBT.class, list -> {
            Subscribers subs = new Subscribers();
            subs.deserializeNBT(list);
            return subs;
        });

        this.donoMap = NBTHelper.readMap(nbt, "StreamDonos", CompoundNBT.class, list -> {
            Donations donos = new Donations();
            donos.deserializeNBT(list);
            return donos;
        });
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        NBTHelper.writeMap(nbt, "StreamSubsBuffer", this.subBufferMap, ListNBT.class, Subscribers::serializeNBT);
        NBTHelper.writeMap(nbt, "StreamSubs", this.subMap, ListNBT.class, Subscribers::serializeNBT);
        NBTHelper.writeMap(nbt, "StreamDonos", this.donoMap, CompoundNBT.class, Donations::serializeNBT);
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

    public static StreamData get(ServerWorld world) {
        return world.getServer().func_241755_D_().getSavedData()
                .getOrCreate(StreamData::new, DATA_NAME);
    }

    public static class Subscribers implements INBTSerializable<ListNBT> {
        private final List<Instance> subs = new ArrayList<>();

        public void onSub(String name, int months) {
            this.subs.add(new Instance(name, months));
        }

        public void onSub(CompoundNBT nbt) {
            Instance sub = new Instance();
            sub.deserializeNBT(nbt);
            this.subs.add(sub);
        }

        public int count() {
            return subs.size();
        }

        public Instance popOneSub() {
            return subs.remove(0);
        }

        @Override
        public ListNBT serializeNBT() {
            return this.subs.stream().map(Instance::serializeNBT).collect(Collectors.toCollection(ListNBT::new));
        }

        @Override
        public void deserializeNBT(ListNBT nbt) {
            this.subs.clear();
            IntStream.range(0, nbt.size()).mapToObj(nbt::getCompound).forEach(this::onSub);
        }

        public static class Instance implements INBTSerializable<CompoundNBT> {
            private String name = "";
            private int months = 0;

            public Instance() {

            }

            public Instance(String name, int months) {
                this.name = name;
                this.months = months;
            }

            public String getName() {
                return this.name;
            }

            public int getMonths() {
                return this.months;
            }

            @Override
            public CompoundNBT serializeNBT() {
                CompoundNBT nbt = new CompoundNBT();
                nbt.putString("Name", this.name);
                nbt.putInt("Months", this.months);
                return nbt;
            }

            @Override
            public void deserializeNBT(CompoundNBT nbt) {
                this.name = nbt.getString("Name");
                this.months = nbt.getInt("Months");
            }
        }
    }

    public static class Donations implements INBTSerializable<CompoundNBT> {
        private final Map<String, Integer> donoMap = new HashMap<>();

        public Donations onDono(String name, int amount) {
            this.donoMap.put(name, this.donoMap.getOrDefault(name, 0) + amount);
            return this;
        }

        public Set<String> getDonators() {
            return donoMap.keySet();
        }

        public String weightedRandom() {
            int totalWeight = donoMap.values().stream().mapToInt(i -> i).sum();
            double r = Math.random() * totalWeight;
            double c = 0.0;
            for (Map.Entry<String, Integer> entry : donoMap.entrySet()) {
                c += entry.getValue();
                if (c >= r) {
                    return entry.getKey();
                }
            }
            throw new RuntimeException("Wat? :O");
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
