package iskallia.vault.network.message;

import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

// From Server to Client
// "Hey dude, your new vault level info is like dis!"
public class VaultLevelMessage {

    public int vaultLevel;
    public int vaultExp, tnl;

    public VaultLevelMessage() { }

    public VaultLevelMessage(int vaultLevel, int vaultExp, int tnl) {
        this.vaultLevel = vaultLevel;
        this.vaultExp = vaultExp;
        this.tnl = tnl;
    }

    public static void encode(VaultLevelMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.vaultLevel);
        buffer.writeInt(message.vaultExp);
        buffer.writeInt(message.tnl);
    }

    public static VaultLevelMessage decode(PacketBuffer buffer) {
        VaultLevelMessage message = new VaultLevelMessage();
        message.vaultLevel = buffer.readInt();
        message.vaultExp = buffer.readInt();
        message.tnl = buffer.readInt();
        return message;
    }

    public static void handle(VaultLevelMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            VaultBarOverlay.vaultLevel = message.vaultLevel;
            VaultBarOverlay.vaultExp = message.vaultExp;
            VaultBarOverlay.tnl = message.tnl;
        });
        context.setPacketHandled(true);
    }

}
