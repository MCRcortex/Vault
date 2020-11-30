package iskallia.vault.vending;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.util.nbt.INBTSerializable;
import iskallia.vault.util.nbt.NBTSerialize;
import iskallia.vault.util.nbt.NBTSerializer;
import iskallia.vault.util.nbt.UnserializableClassException;
import net.minecraft.nbt.CompoundNBT;

public class TraderCore implements INBTSerializable {

    @Expose
    @NBTSerialize
    private String NAME;
    @Expose
    @NBTSerialize
    private Trade TRADE;

    public TraderCore(String name, Trade trade) {
        this.NAME = name;
        this.TRADE = trade;
    }

    public TraderCore() {

    }

    public static CompoundNBT writeToNBT(TraderCore core) {
        CompoundNBT nbt;
        try {
            nbt = NBTSerializer.serialize(core);
        } catch (UnserializableClassException | IllegalAccessException e) {
            if (e instanceof UnserializableClassException) {
                Vault.LOGGER.error("Failed to serialize: " + ((UnserializableClassException) e).getOffendingClass());
            } else {
                e.printStackTrace();
            }
            return null;
        }
        return nbt;
    }

    public static TraderCore readFromNBT(CompoundNBT nbt) {
        TraderCore core;
        try {
            core = NBTSerializer.deserialize(TraderCore.class, nbt);
        } catch (UnserializableClassException | IllegalAccessException | InstantiationException e) {
            if (e instanceof UnserializableClassException) {
                Vault.LOGGER.error("Failed to deserialize: " + ((UnserializableClassException) e).getOffendingClass());
            } else {
                e.printStackTrace();
            }
            return null;
        }
        return core;
    }

    public String getName() {
        return this.NAME;
    }

    public void setName(String name) {
        this.NAME = name;
    }

    public Trade getTrade() {
        return this.TRADE;
    }

    public void setTrade(Trade trade) {
        this.TRADE = trade;
    }

}
