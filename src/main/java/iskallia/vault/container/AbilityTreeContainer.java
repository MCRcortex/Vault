package iskallia.vault.container;

import iskallia.vault.ability.AbilityTree;
import iskallia.vault.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

public class AbilityTreeContainer extends Container {

    private AbilityTree abilityTree;

    public AbilityTreeContainer(int windowId, AbilityTree abilityTree) {
        super(ModContainers.abilityTree, windowId);
        this.abilityTree = abilityTree;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    public AbilityTree getAbilityTree() {
        return abilityTree;
    }

}
