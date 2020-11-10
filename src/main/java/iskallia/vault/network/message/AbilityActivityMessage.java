package iskallia.vault.network.message;

import iskallia.vault.client.gui.overlay.AbilitiesOverlay;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

// From Server to Client
// "Hey dude, dis is your new ability cooldown and active state"
public class AbilityActivityMessage {

    public int cooldownTicks;
    public boolean active;

    public AbilityActivityMessage() { }

    public AbilityActivityMessage(int cooldownTicks, boolean active) {
        this.cooldownTicks = cooldownTicks;
        this.active = active;
    }

    public static void encode(AbilityActivityMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.cooldownTicks);
        buffer.writeBoolean(message.active);
    }

    public static AbilityActivityMessage decode(PacketBuffer buffer) {
        AbilityActivityMessage message = new AbilityActivityMessage();
        message.cooldownTicks = buffer.readInt();
        message.active = buffer.readBoolean();
        return message;
    }

    public static void handle(AbilityActivityMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            AbilitiesOverlay.cooldownTicks = message.cooldownTicks;
            AbilitiesOverlay.active = message.active;
        });
        context.setPacketHandled(true);
    }

}
