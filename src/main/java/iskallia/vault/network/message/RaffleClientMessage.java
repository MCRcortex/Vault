package iskallia.vault.network.message;

import iskallia.vault.client.gui.screen.RaffleScreen;
import iskallia.vault.util.nbt.NBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class RaffleClientMessage {

    public enum Opcode {
        OPEN_UI
    }

    public Opcode opcode;
    public CompoundNBT payload;

    public RaffleClientMessage() { }

    public static void encode(RaffleClientMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.opcode.ordinal());
        buffer.writeCompoundTag(message.payload);
    }

    public static RaffleClientMessage decode(PacketBuffer buffer) {
        RaffleClientMessage message = new RaffleClientMessage();
        message.opcode = Opcode.values()[buffer.readInt()];
        message.payload = buffer.readCompoundTag();
        return message;
    }

    public static void handle(RaffleClientMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (message.opcode == Opcode.OPEN_UI) {
                List<String> occupants = NBTHelper.readList(message.payload, "Occupants", StringNBT.class, StringNBT::getString);
                String winner = message.payload.getString("Winner");
                displayGUI(occupants, winner);
            }
        });
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void displayGUI(List<String> occupants, String winner) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.displayGuiScreen(new RaffleScreen(occupants, winner));
    }

    public static RaffleClientMessage openUI(Collection<String> occupants, String winner) {
        RaffleClientMessage message = new RaffleClientMessage();
        message.opcode = Opcode.OPEN_UI;
        message.payload = new CompoundNBT();
        NBTHelper.writeList(message.payload, "Occupants", occupants, StringNBT.class, StringNBT::valueOf);
        message.payload.putString("Winner", winner);
        return message;
    }

}
