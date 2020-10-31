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

    @Override
    protected void reset() {
        this.MOD_RESEARCHES = new LinkedList<>();
        ModResearch sampleModResearch = new ModResearch("Vault Knowledge", 1, Vault.MOD_ID);
        sampleModResearch.getRestrictions().set(Restrictions.Type.USABILITY, true);
        this.MOD_RESEARCHES.add(sampleModResearch);

        this.CUSTOM_RESEARCHES = new LinkedList<>();
        this.CUSTOM_RESEARCHES.add(new CustomResearch("Pickaxe Proficiency I", 1,
                new String[]{"minecraft:wooden_pickaxe",
                        "minecraft:stone_pickaxe",
                        "minecraft:iron_pickaxe",
                        "minecraft:diamond_pickaxe"},
                new String[]{},
                new String[]{}));
    }

}
