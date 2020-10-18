package iskallia.vault.init;

import iskallia.vault.ability.AbilityTree;
import iskallia.vault.container.AbilityTreeContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Optional;
import java.util.UUID;

public class ModContainers {

    public static ContainerType<AbilityTreeContainer> abilityTree;

    public static void register(IForgeRegistry<ContainerType<?>> registry) {
        abilityTree = createContainerType((windowId, inventory, buffer) -> {
            UUID uniqueID = inventory.player.getUniqueID();
            AbilityTree abilityTree = new AbilityTree(uniqueID);
            abilityTree.deserializeNBT(Optional.ofNullable(buffer.readCompoundTag()).orElse(new CompoundNBT()));
            return new AbilityTreeContainer(windowId, abilityTree);
        });

        registry.registerAll(
                abilityTree.setRegistryName("ability_tree")
        );
    }

    private static <T extends Container> ContainerType<T> createContainerType(IContainerFactory<T> factory) {
        return new ContainerType<T>(factory);
    }

}