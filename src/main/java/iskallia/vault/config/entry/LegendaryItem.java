package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;

public class LegendaryItem {

    @Expose public String ITEM;
    @Expose public String NBT;

    public LegendaryItem(String item, String nbt) {
        this.ITEM = item;
        this.NBT = nbt;
    }

}
