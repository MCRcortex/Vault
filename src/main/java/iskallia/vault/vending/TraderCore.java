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
    private boolean MEGAHEAD;
    @Expose
    @NBTSerialize
    private Trade TRADE;

    public TraderCore(String name, Trade trade) {
        this(name, trade, false);
    }

    public TraderCore(String name, Trade trade, boolean megahead) {
        this.NAME = name;
        this.MEGAHEAD = megahead;
        this.TRADE = trade;
    }

    public TraderCore() { }

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

    public boolean isMegahead() {
        return MEGAHEAD;
    }

    public void setMegahead(boolean megahead) {
        this.MEGAHEAD = megahead;
    }

}
