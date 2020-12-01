package iskallia.vault.vending;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.nbt.INBTSerializable;
import iskallia.vault.util.nbt.NBTSerialize;

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
