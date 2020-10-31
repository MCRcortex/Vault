package iskallia.vault.research.node;

import iskallia.vault.research.Restrictions;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

public class CustomResearch extends Research {

    public CustomResearch(String name, int cost, String[] itemIds, String[] blockIds, String[] entityIds) {
        super(name, cost);

        // TODO: Implement this dude again..
    }

    @Override
    public boolean restricts(Item item, Restrictions.Type restrictionType) {
        return false;
    }

    @Override
    public boolean restricts(Block block, Restrictions.Type restrictionType) {
        return false;
    }

    @Override
    public boolean restricts(EntityType<?> entityType, Restrictions.Type restrictionType) {
        return false;
    }

}
