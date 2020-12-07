package iskallia.vault.world.data;

import iskallia.vault.Vault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import java.time.Instant;

public class GlobalTimeData extends WorldSavedData {

    protected static final String DATA_NAME = Vault.MOD_ID + "_GlobalTime";

    private long startTime;
    private long endTime;
    private long additionalTime;

    public GlobalTimeData() {
        this(DATA_NAME);
        startTime = Instant.now().getEpochSecond();
        endTime = startTime + 7776000L;
    }

    public GlobalTimeData(String name) {
        super(name);
    }

    public long getTimeRemaining() {
        return endTime - startTime + additionalTime;
    }

    public void addTime(long seconds) {
        this.additionalTime += seconds;
    }

    @Override
    public void read(CompoundNBT nbt) {
        startTime = nbt.getLong("startTime");
        endTime = nbt.getLong("endTime");
        additionalTime = nbt.getLong("additionalTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putLong("startTime", startTime);
        nbt.putLong("endTime", endTime);
        nbt.putLong("additionalTime", additionalTime);
        return nbt;
    }

    public static GlobalTimeData get(ServerWorld world) {
        return world.getServer().func_241755_D_().getSavedData().getOrCreate(GlobalTimeData::new, DATA_NAME);
    }

}
