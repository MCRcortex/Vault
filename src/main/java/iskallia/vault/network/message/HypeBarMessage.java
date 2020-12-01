package iskallia.vault.network.message;

import iskallia.vault.client.gui.overlay.HyperBarOverlay;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

// From Server to Client
// "Yo dude, this is your new Hypebar stuff"
public class HypeBarMessage {

    public int currentHype;
    public int maxHype;

    public HypeBarMessage() { }

    public HypeBarMessage(int currentHype, int maxHype) {
        this.currentHype = currentHype;
        this.maxHype = maxHype;
    }

    public static void encode(HypeBarMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.currentHype);
        buffer.writeInt(message.maxHype);
    }

    public static HypeBarMessage decode(PacketBuffer buffer) {
        HypeBarMessage message = new HypeBarMessage();
        message.currentHype = buffer.readInt();
        message.maxHype = buffer.readInt();
        return message;
    }

    public static void handle(HypeBarMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            HyperBarOverlay.currentHype = message.currentHype;
            HyperBarOverlay.maxHype = message.maxHype;
        });
        context.setPacketHandled(true);
    }

}
