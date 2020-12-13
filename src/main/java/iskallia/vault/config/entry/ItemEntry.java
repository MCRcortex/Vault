package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;

public class ItemEntry {

    @Expose public String ITEM;
    @Expose public String NBT;

    public ItemEntry(String item, String nbt) {
        this.ITEM = item;
        this.NBT = nbt;
    }

}
