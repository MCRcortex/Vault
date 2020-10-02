package iskallia.vault.event;

import iskallia.vault.init.ModContainers;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class InitRegistryEvent {

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) { }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) { }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) { }

    @SubscribeEvent
    public static void onContainerRegister(RegistryEvent.Register<ContainerType<?>> event) {
        ModContainers.register(event.getRegistry());
    }

}
