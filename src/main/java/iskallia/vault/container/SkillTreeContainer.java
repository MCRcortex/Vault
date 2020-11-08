package iskallia.vault.container;

import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.init.ModContainers;
import iskallia.vault.research.ResearchTree;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

// Wutax calls this iskall-proofing
// I call that my stupidity XD --iGoodie
public class SkillTreeContainer extends Container {

    private TalentTree talentTree;
    private ResearchTree researchTree;

    public SkillTreeContainer(int windowId, TalentTree talentTree, ResearchTree researchTree) {
        super(ModContainers.SKILL_TREE_CONTAINER, windowId);
        this.talentTree = talentTree;
        this.researchTree = researchTree;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    public TalentTree getTalentTree() {
        return talentTree;
    }

    public ResearchTree getResearchTree() {
        return researchTree;
    }

}