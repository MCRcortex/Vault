package iskallia.vault.util.lambda;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;

@FunctionalInterface
public interface ContainerFactoryLambda<C extends Container> {

    C build(int windowId, PlayerInventory inventory, PacketBuffer data);

}
