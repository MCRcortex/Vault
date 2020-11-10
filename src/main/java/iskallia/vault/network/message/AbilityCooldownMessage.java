package iskallia.vault.network.message;

import iskallia.vault.client.gui.overlay.AbilitiesOverlay;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

// From Server to Client
// "Hey dude, you have dis much cooldown"
public class AbilityCooldownMessage {

    public int cooldownTicks;

    public AbilityCooldownMessage() { }

    public AbilityCooldownMessage(int focusedIndex) {
        this.cooldownTicks = focusedIndex;
    }

    public static void encode(AbilityCooldownMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.cooldownTicks);
    }

    public static AbilityCooldownMessage decode(PacketBuffer buffer) {
        AbilityCooldownMessage message = new AbilityCooldownMessage();
        message.cooldownTicks = buffer.readInt();
        return message;
    }

    public static void handle(AbilityCooldownMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            AbilitiesOverlay.cooldownTicks = message.cooldownTicks;
        });
        context.setPacketHandled(true);
    }

}
