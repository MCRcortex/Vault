package iskallia.vault.network.message;

import iskallia.vault.client.gui.overlay.ArenaScoreboardOverlay;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

// From Server to Client
// "Yo dude, this dude dealt dis much damage"
public class ScoreboardDamageMessage {

    public String nickname;
    public float damageDealt;

    public ScoreboardDamageMessage() { }

    public ScoreboardDamageMessage(String nickname, float damageDealt) {
        this.nickname = nickname;
        this.damageDealt = damageDealt;
    }

    public static void encode(ScoreboardDamageMessage message, PacketBuffer buffer) {
        buffer.writeString(message.nickname, 32767);
        buffer.writeFloat(message.damageDealt);
    }

    public static ScoreboardDamageMessage decode(PacketBuffer buffer) {
        ScoreboardDamageMessage message = new ScoreboardDamageMessage();
        message.nickname = buffer.readString(32767);
        message.damageDealt = buffer.readFloat();
        return message;
    }

    public static void handle(ScoreboardDamageMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            System.out.println(message.nickname + " dealt: " + message.damageDealt);
            ArenaScoreboardOverlay.scoreboard.onDamageDealt(message.nickname, message.damageDealt);
        });
        context.setPacketHandled(true);
    }

}
