package iskallia.vault.container;

import iskallia.vault.ability.AbilityTree;
import iskallia.vault.init.ModContainers;
import iskallia.vault.research.ResearchTree;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

// Wutax calls this iskall-proofing
// I call that my stupidity XD --iGoodie
public class SkillTreeContainer extends Container {

    private AbilityTree abilityTree;
    private ResearchTree researchTree;

    public SkillTreeContainer(int windowId, AbilityTree abilityTree, ResearchTree researchTree) {
        super(ModContainers.SKILL_TREE_CONTAINER, windowId);
        this.abilityTree = abilityTree;
        this.researchTree = researchTree;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    public AbilityTree getAbilityTree() {
        return abilityTree;
    }

    public ResearchTree getResearchTree() {
        return researchTree;
    }

}
