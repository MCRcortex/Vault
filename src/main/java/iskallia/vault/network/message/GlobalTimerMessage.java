package iskallia.vault.network.message;

import iskallia.vault.client.gui.screen.GlobalTimerScreen;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.base.OpcodeMessage;
import iskallia.vault.world.data.GlobalTimeData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class GlobalTimerMessage extends OpcodeMessage<GlobalTimerMessage.Opcode> {

    enum Opcode {
        REQUEST_UNIX,
        OPEN_UI
    }

    public static void encode(GlobalTimerMessage message, PacketBuffer buffer) {
        message.encodeSelf(message, buffer);
    }

    public static GlobalTimerMessage decode(PacketBuffer buffer) {
        GlobalTimerMessage message = new GlobalTimerMessage();
        message.decodeSelf(buffer, Opcode.class);
        return message;
    }

    public static void handle(GlobalTimerMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (message.opcode == Opcode.REQUEST_UNIX) {
                ServerPlayerEntity sender = context.getSender();
                GlobalTimeData globalTimeData = GlobalTimeData.get(sender.getServerWorld());
                ModNetwork.CHANNEL.sendTo(
                        openUI(globalTimeData.getEndTime()),
                        sender.connection.netManager,
                        NetworkDirection.PLAY_TO_CLIENT
                );

            } else if (message.opcode == Opcode.OPEN_UI) {
                displayGUI(message.payload.getLong("EndUnix"));
            }
        });
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void displayGUI(long endUnix) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.displayGuiScreen(new GlobalTimerScreen(endUnix));
    }

    public static GlobalTimerMessage requestUnix() {
        return composeMessage(new GlobalTimerMessage(), Opcode.REQUEST_UNIX, payload -> {});
    }

    public static GlobalTimerMessage openUI(long endUnix) {
        return composeMessage(new GlobalTimerMessage(), Opcode.OPEN_UI, payload -> {
            payload.putLong("EndUnix", endUnix);
        });
    }

}
