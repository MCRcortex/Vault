package iskallia.vault.vending;

import com.google.gson.annotations.Expose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class Trade implements INBTSerializable<CompoundNBT> {

    @Expose
    protected Product buy;
    @Expose
    protected Product extra;
    @Expose
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

    @Override
    public CompoundNBT serializeNBT() {

        CompoundNBT nbt = new CompoundNBT();
        if (this.buy != null)
            nbt.put("buy", buy.serializeNBT());
        if (this.extra != null)
            nbt.put("extra", extra.serializeNBT());
        if (this.sell != null)
            nbt.put("sell", sell.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.buy = new Product();
        this.extra = new Product();
        this.sell = new Product();
        if (nbt.contains("buy")) buy.deserializeNBT(nbt.getCompound("buy"));
        else buy = null;
        if (nbt.contains("extra")) extra.deserializeNBT(nbt.getCompound("extra"));
        else extra = null;
        if (nbt.contains("sell")) sell.deserializeNBT(nbt.getCompound("sell"));
        else sell = null;
    }
}
