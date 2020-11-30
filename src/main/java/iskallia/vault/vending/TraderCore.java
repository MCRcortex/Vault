package iskallia.vault.vending;

import com.google.gson.annotations.Expose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class TraderCore implements INBTSerializable<CompoundNBT> {

    @Expose
    private String NAME;
    @Expose
    private Trade TRADE;

    public TraderCore(String name, Trade trade) {
        this.NAME = name;
        this.TRADE = trade;
    }

    private TraderCore() {

    }

    public static TraderCore getCoreFromNBT(CompoundNBT nbt) {
        TraderCore core = new TraderCore();
        core.deserializeNBT(nbt);
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

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        if (this.NAME != null)
            nbt.putString("name", this.NAME);
        if (this.TRADE != null)
            nbt.put("trade", this.TRADE.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.NAME = nbt.getString("name");
        this.TRADE = new Trade();
        this.TRADE.deserializeNBT(nbt.getCompound("trade"));
    }
}
