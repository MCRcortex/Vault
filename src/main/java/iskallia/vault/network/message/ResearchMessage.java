package iskallia.vault.network.message;

import iskallia.vault.ability.AbilityTree;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.research.node.Research;
import iskallia.vault.world.data.PlayerAbilitiesData;
import iskallia.vault.world.data.PlayerResearchesData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

// From Client to Server
// "Hey dude, I want to research dis. May I?"
public class ResearchMessage {

    public String researchName;

    public ResearchMessage() { }

    public ResearchMessage(String researchName) {
        this.researchName = researchName;
    }

    public static void encode(ResearchMessage message, PacketBuffer buffer) {
        buffer.writeString(message.researchName);
    }

    public static ResearchMessage decode(PacketBuffer buffer) {
        ResearchMessage message = new ResearchMessage();
        message.researchName = buffer.readString();
        return message;
    }

    public static void handle(ResearchMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity sender = context.getSender();

            if (sender == null) return;

            Research research = ModConfigs.RESEARCHES.getByName(message.researchName);

            if (research == null) return;

            PlayerAbilitiesData abilitiesData = PlayerAbilitiesData.get((ServerWorld) sender.world);
            PlayerResearchesData researchesData = PlayerResearchesData.get((ServerWorld) sender.world);
            AbilityTree abilityTree = abilitiesData.getAbilities(sender);
            ResearchTree researchTree = researchesData.getResearches(sender);

            if (abilityTree.getUnspentSkillPts() >= research.getCost()) {
                researchesData.research(sender, research);
                abilitiesData.spendSkillPts(sender, research.getCost());
            }
        });
        context.setPacketHandled(true);
    }

}
