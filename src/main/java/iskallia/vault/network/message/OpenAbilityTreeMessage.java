package iskallia.vault.network.message;

import iskallia.vault.ability.AbilityTree;
import iskallia.vault.container.AbilityTreeContainer;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.function.Supplier;

// From Client to Server
public class OpenAbilityTreeMessage {

    public OpenAbilityTreeMessage() { }

    public static void encode(OpenAbilityTreeMessage message, PacketBuffer buffer) { }

    public static OpenAbilityTreeMessage decode(PacketBuffer buffer) {
        return new OpenAbilityTreeMessage();
    }

    public static void handle(OpenAbilityTreeMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity sender = context.getSender();
            NetworkHooks.openGui(sender, new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent("container.vault.ability_tree");
                }

                @Nullable
                @Override
                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    PlayerAbilitiesData playerAbilitiesData = PlayerAbilitiesData.get((ServerWorld) playerEntity.world);
                    AbilityTree abilities = playerAbilitiesData.getAbilities(sender.getUniqueID());
                    return new AbilityTreeContainer(i, abilities);
                }
            });
        });
        context.setPacketHandled(true);
    }

}
