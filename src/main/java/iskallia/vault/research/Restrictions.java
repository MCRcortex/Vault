package iskallia.vault.research;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class Restrictions {

    @Expose protected Map<Type, Boolean> restricts;

    public Restrictions() {
        this.restricts = new HashMap<>();

        for (Type type : Type.values()) {
            restricts.put(type, false);
        }
    }

    public void set(Type type, boolean restricts) {
        this.restricts.put(type, restricts);
    }

    public boolean restricts(Type type) {
        return this.restricts.getOrDefault(type, false);
    }

    public enum Type {
        USABILITY, // Right click with an item, or placement with blocks
        CRAFTABILITY, // Crafting an item using Craft-Matrix
        HITTABILITY, // Left click on a block in the world
        BLOCK_INTERACTABILITY, // Right click on a block in the world
        ENTITY_INTERACTABILITY, // Right click on an entity in the world
    }

}
