package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.network.message.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetwork {

    private static final String NETWORK_VERSION = "0.11.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Vault.MOD_ID, "network"),
            () -> NETWORK_VERSION,
            version -> version.equals(NETWORK_VERSION), // Client acceptance predicate
            version -> version.equals(NETWORK_VERSION) // Server acceptance predicate
    );

    public static void initialize() {
        CHANNEL.registerMessage(0, OpenSkillTreeMessage.class,
                OpenSkillTreeMessage::encode,
                OpenSkillTreeMessage::decode,
                OpenSkillTreeMessage::handle);

        CHANNEL.registerMessage(1, VaultLevelMessage.class,
                VaultLevelMessage::encode,
                VaultLevelMessage::decode,
                VaultLevelMessage::handle);

        CHANNEL.registerMessage(2, TalentUpgradeMessage.class,
                TalentUpgradeMessage::encode,
                TalentUpgradeMessage::decode,
                TalentUpgradeMessage::handle);

        CHANNEL.registerMessage(3, ResearchMessage.class,
                ResearchMessage::encode,
                ResearchMessage::decode,
                ResearchMessage::handle);

        CHANNEL.registerMessage(4, ResearchTreeMessage.class,
                ResearchTreeMessage::encode,
                ResearchTreeMessage::decode,
                ResearchTreeMessage::handle);

        CHANNEL.registerMessage(5, AbilityKeyMessage.class,
                AbilityKeyMessage::encode,
                AbilityKeyMessage::decode,
                AbilityKeyMessage::handle);

        CHANNEL.registerMessage(6, AbilityUpgradeMessage.class,
                AbilityUpgradeMessage::encode,
                AbilityUpgradeMessage::decode,
                AbilityUpgradeMessage::handle);

        CHANNEL.registerMessage(7, AbilityKnownOnesMessage.class,
                AbilityKnownOnesMessage::encode,
                AbilityKnownOnesMessage::decode,
                AbilityKnownOnesMessage::handle);

        CHANNEL.registerMessage(8, AbilityFocusMessage.class,
                AbilityFocusMessage::encode,
                AbilityFocusMessage::decode,
                AbilityFocusMessage::handle);

        CHANNEL.registerMessage(9, AbilityActivityMessage.class,
                AbilityActivityMessage::encode,
                AbilityActivityMessage::decode,
                AbilityActivityMessage::handle);

        CHANNEL.registerMessage(10, VaultRaidTickMessage.class,
                VaultRaidTickMessage::encode,
                VaultRaidTickMessage::decode,
                VaultRaidTickMessage::handle);

        CHANNEL.registerMessage(11, FighterSizeMessage.class,
                FighterSizeMessage::encode,
                FighterSizeMessage::decode,
                FighterSizeMessage::handle);

        CHANNEL.registerMessage(12, ScoreboardDamageMessage.class,
                ScoreboardDamageMessage::encode,
                ScoreboardDamageMessage::decode,
                ScoreboardDamageMessage::handle);

        CHANNEL.registerMessage(13, HypeBarMessage.class,
                HypeBarMessage::encode,
                HypeBarMessage::decode,
                HypeBarMessage::handle);
    }

}
