package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.research.Restrictions;
import iskallia.vault.research.node.CustomResearch;
import iskallia.vault.research.node.ModResearch;
import iskallia.vault.research.node.Research;

import java.util.LinkedList;
import java.util.List;

public class ResearchConfig extends Config {

    @Expose public List<ModResearch> MOD_RESEARCHES;
    @Expose public List<CustomResearch> CUSTOM_RESEARCHES;

    @Override
    public String getName() {
        return "researches";
    }

    public List<Research> getAll() {
        List<Research> all = new LinkedList<>();
        all.addAll(MOD_RESEARCHES);
        all.addAll(CUSTOM_RESEARCHES);
        return all;
    }

    public Research getByName(String name) {
        for (Research research : getAll()) {
            if (research.getName().equals(name))
                return research;
        }
        return null;
    }

    @Override
    protected void reset() {
        this.MOD_RESEARCHES = new LinkedList<>();
        this.MOD_RESEARCHES.add(new ModResearch("Backpacks!", 2, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Waystones", 3, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Safety First", 3, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Organisation", 3, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Super Builder", 3, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Super Miner", 3, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Storage Noob", 1, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Storage Master", 2, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Storage Refined", 6, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Storage Energistic", 6, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Storage Enthusiast", 4, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Decorator", 1, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Decorator Pro", 2, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Engineer", 1, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Super Engineer", 3, ""));
        this.MOD_RESEARCHES.add(new ModResearch("One with Ender", 1, ""));
        this.MOD_RESEARCHES.add(new ModResearch("The Chef", 1, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Traveller", 1, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Adventurer", 3, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Hacker", 6, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Redstoner", 1, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Natural Magical", 8, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Tech Freak", 10, ""));
        this.MOD_RESEARCHES.add(new ModResearch("The Emerald King", 3, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Quarry", 6, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Spaceman", 4, ""));
        this.MOD_RESEARCHES.add(new ModResearch("Total Control", 3, ""));

        this.CUSTOM_RESEARCHES = new LinkedList<>();
//        this.CUSTOM_RESEARCHES.add(new CustomResearch("Pickaxe Proficiency I", 1,
//                new String[]{"minecraft:wooden_pickaxe",
//                        "minecraft:stone_pickaxe",
//                        "minecraft:iron_pickaxe",
//                        "minecraft:diamond_pickaxe"},
//                new String[]{},
//                new String[]{}));
    }

}
