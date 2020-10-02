package iskallia.vault.init;

import iskallia.vault.ability.AbilityTree;
import iskallia.vault.container.AbilityTreeContainer;
import iskallia.vault.util.lambda.ContainerFactoryLambda;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.UUID;

public class ModContainers {

    public static ContainerType<AbilityTreeContainer> abilityTree;

    public static void register(IForgeRegistry<ContainerType<?>> registry) {
        abilityTree = createContainerType((windowId, inventory, data) -> {
            // TODO: Get player abilities, then pass to the container below
            UUID uniqueID = inventory.player.getUniqueID();
            return new AbilityTreeContainer(windowId, new AbilityTree(uniqueID));
        });
    }

    @SuppressWarnings("unchecked")
    private static <T extends Container>
    ContainerType<T>
    createContainerType(ContainerFactoryLambda<T> lambda) {
        return new ContainerType<T>((ContainerType.IFactory<T>) lambda);
    }

}