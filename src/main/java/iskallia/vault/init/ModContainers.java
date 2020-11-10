package iskallia.vault.init;

import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.container.SkillTreeContainer;
import iskallia.vault.research.ResearchTree;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Optional;
import java.util.UUID;

public class ModContainers {

    public static ContainerType<SkillTreeContainer> SKILL_TREE_CONTAINER;

    public static void register(IForgeRegistry<ContainerType<?>> registry) {
        SKILL_TREE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            UUID uniqueID = inventory.player.getUniqueID();
            AbilityTree abilityTree = new AbilityTree(uniqueID);
            abilityTree.deserializeNBT(Optional.ofNullable(buffer.readCompoundTag()).orElse(new CompoundNBT()));
            TalentTree talentTree = new TalentTree(uniqueID);
            talentTree.deserializeNBT(Optional.ofNullable(buffer.readCompoundTag()).orElse(new CompoundNBT()));
            ResearchTree researchTree = new ResearchTree(uniqueID);
            researchTree.deserializeNBT(Optional.ofNullable(buffer.readCompoundTag()).orElse(new CompoundNBT()));
            return new SkillTreeContainer(windowId, abilityTree, talentTree, researchTree);
        });

        registry.registerAll(
                SKILL_TREE_CONTAINER.setRegistryName("ability_tree")
        );
    }

    private static <T extends Container> ContainerType<T> createContainerType(IContainerFactory<T> factory) {
        return new ContainerType<T>(factory);
    }

}