package iskallia.vault.init;

import iskallia.vault.container.SkillTreeContainer;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.talent.TalentTree;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.network.IContainerFactory;

import java.util.Optional;
import java.util.UUID;

public class ModContainers {

    public static ContainerType<SkillTreeContainer> SKILL_TREE_CONTAINER;

    public static void register(RegistryEvent.Register<ContainerType<?>> event) {
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

        event.getRegistry().registerAll(
                SKILL_TREE_CONTAINER.setRegistryName("ability_tree")
        );
    }

    private static <T extends Container> ContainerType<T> createContainerType(IContainerFactory<T> factory) {
        return new ContainerType<T>(factory);
    }

}