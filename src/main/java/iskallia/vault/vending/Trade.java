package iskallia.vault.vending;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.nbt.INBTSerializable;
import iskallia.vault.util.nbt.NBTSerialize;

public class Trade implements INBTSerializable {

    @Expose
    @NBTSerialize
    protected Product buy;
    @Expose
    @NBTSerialize
    protected Product extra;
    @Expose
    @NBTSerialize
    protected Product sell;
    private int hashCode;

    public Trade() {
        // Serialization.
    }

    public Trade(Product buy, Product extra, Product sell) {
        this.buy = buy;
        this.extra = extra;
        this.sell = sell;
    }

    public Product getBuy() {
        return this.buy;
    }

    public Product getExtra() {
        return this.extra;
    }

    public Product getSell() {
        return this.sell;
    }

    public boolean isValid() {
        if (this.buy == null || !this.buy.isValid())
            return false;
        if (this.sell == null || !this.sell.isValid())
            return false;
        if (this.extra != null && !this.extra.isValid())
            return false;
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        else if (obj == this)
            return true;
        else if (this.getClass() != obj.getClass())
            return false;

        Trade trade = (Trade) obj;
        return trade.sell.equals(this.sell) && trade.buy.equals(this.buy);
    }

}
