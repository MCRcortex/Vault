package iskallia.vault.network.message;

import iskallia.vault.client.gui.screen.RaffleScreen;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.item.ItemVaultCrystal;
import iskallia.vault.util.nbt.NBTHelper;
import iskallia.vault.world.data.StreamData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class RaffleMessage {

    public enum Opcode {
        REQUEST_RAFFLE(LogicalSide.SERVER),
        OPEN_UI(LogicalSide.CLIENT),
        DONE_ANIMATING(LogicalSide.SERVER);

        LogicalSide receivingSide;

        Opcode(LogicalSide side) {
            this.receivingSide = side;
        }
    }

    public Opcode opcode;
    public CompoundNBT payload;

    public RaffleMessage() { }

    public static void encode(RaffleMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.opcode.ordinal());
        buffer.writeCompoundTag(message.payload);
    }

    public static RaffleMessage decode(PacketBuffer buffer) {
        RaffleMessage message = new RaffleMessage();
        message.opcode = Opcode.values()[buffer.readInt()];
        message.payload = buffer.readCompoundTag();
        return message;
    }

    public static void handle(RaffleMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (message.opcode == Opcode.REQUEST_RAFFLE) {
                ServerPlayerEntity sender = context.getSender();
                StreamData streamData = StreamData.get(sender.getServerWorld());
                StreamData.Donations donations = streamData.getDonations(sender.getUniqueID());
                if (donations.getDonators().size() < 5) {
                    StringTextComponent text = new StringTextComponent("Not enough people for Raffle");
                    text.setStyle(Style.EMPTY.setColor(Color.fromInt(0xFF_ff9966)));
                    sender.sendStatusMessage(text, true);

                } else {
                    ModNetwork.CHANNEL.sendTo(
                            RaffleMessage.openUI(donations.getDonators(), donations.weightedRandom()),
                            sender.connection.netManager,
                            NetworkDirection.PLAY_TO_CLIENT
                    );
                }

            } else if (message.opcode == Opcode.OPEN_UI) {
                Minecraft minecraft = Minecraft.getInstance();
                List<String> occupants = NBTHelper.readList(message.payload, "Occupants", StringNBT.class, StringNBT::getString);
                String winner = message.payload.getString("Winner");
                minecraft.displayGuiScreen(new RaffleScreen(occupants, winner));

            } else if (message.opcode == Opcode.DONE_ANIMATING) {
                ServerPlayerEntity sender = context.getSender();
                StreamData streamData = StreamData.get(sender.getServerWorld());
                streamData.reset(sender.getServer(), sender.getUniqueID());
                ItemStack vaultCrystal = ItemVaultCrystal.getCrystalWithBoss(message.payload.getString("Winner"));
                sender.dropItem(vaultCrystal, false, false);
            }
        });
        context.setPacketHandled(true);
    }

    public static RaffleMessage requestRaffle() {
        RaffleMessage message = new RaffleMessage();
        message.opcode = Opcode.REQUEST_RAFFLE;
        message.payload = new CompoundNBT();
        return message;
    }

    public static RaffleMessage openUI(Collection<String> occupants, String winner) {
        RaffleMessage message = new RaffleMessage();
        message.opcode = Opcode.OPEN_UI;
        message.payload = new CompoundNBT();
        NBTHelper.writeList(message.payload, "Occupants", occupants, StringNBT.class, StringNBT::valueOf);
        message.payload.putString("Winner", winner);
        return message;
    }

    public static RaffleMessage animationDone(String winner) {
        RaffleMessage message = new RaffleMessage();
        message.opcode = Opcode.DONE_ANIMATING;
        message.payload = new CompoundNBT();
        message.payload.putString("Winner", winner);
        return message;
    }

}
