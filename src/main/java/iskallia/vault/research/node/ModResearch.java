package iskallia.vault.research.node;

import com.google.gson.annotations.Expose;
import iskallia.vault.research.Restrictions;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ModResearch extends Research {

    @Expose protected Set<String> modIds;
    @Expose protected Restrictions restrictions;

    public ModResearch(String name, int cost, String... modIds) {
        super(name, cost);
        this.modIds = new HashSet<>();
        this.restrictions = new Restrictions();

        Collections.addAll(this.modIds, modIds);
    }

    public Set<String> getModIds() {
        return modIds;
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }

    @Override
    public boolean restricts(Item item, Restrictions.Type restrictionType) {
        if (!this.restrictions.restricts(restrictionType)) return false;
        ResourceLocation registryName = item.getRegistryName();
        if (registryName == null) return false;
        return modIds.contains(registryName.getNamespace());
    }

    @Override
    public boolean restricts(Block block, Restrictions.Type restrictionType) {
        if (!this.restrictions.restricts(restrictionType)) return false;
        ResourceLocation registryName = block.getRegistryName();
        if (registryName == null) return false;
        return modIds.contains(registryName.getNamespace());
    }

    @Override
    public boolean restricts(EntityType<?> entityType, Restrictions.Type restrictionType) {
        if (!this.restrictions.restricts(restrictionType)) return false;
        ResourceLocation registryName = entityType.getRegistryName();
        if (registryName == null) return false;
        return modIds.contains(registryName.getNamespace());
    }

}
