package iskallia.vault.network;

import iskallia.vault.Vault;
import iskallia.vault.network.message.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetwork {

    private static final String NETWORK_VERSION = "0.6.0";

    public static final SimpleChannel channel = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Vault.MOD_ID, "network"),
            () -> NETWORK_VERSION,
            version -> version.equals(NETWORK_VERSION), // Client acceptance predicate
            version -> version.equals(NETWORK_VERSION) // Server acceptance predicate
    );

    public static void initialize() {
        channel.registerMessage(0, OpenAbilityTreeMessage.class,
                OpenAbilityTreeMessage::encode,
                OpenAbilityTreeMessage::decode,
                OpenAbilityTreeMessage::handle);

        channel.registerMessage(1, VaultLevelMessage.class,
                VaultLevelMessage::encode,
                VaultLevelMessage::decode,
                VaultLevelMessage::handle);

        channel.registerMessage(2, TalentUpgradeMessage.class,
                TalentUpgradeMessage::encode,
                TalentUpgradeMessage::decode,
                TalentUpgradeMessage::handle);

        channel.registerMessage(3, ResearchMessage.class,
                ResearchMessage::encode,
                ResearchMessage::decode,
                ResearchMessage::handle);

        channel.registerMessage(4, ResearchTreeMessage.class,
                ResearchTreeMessage::encode,
                ResearchTreeMessage::decode,
                ResearchTreeMessage::handle);

        channel.registerMessage(5, AbilityKeyMessage.class,
                AbilityKeyMessage::encode,
                AbilityKeyMessage::decode,
                AbilityKeyMessage::handle);
    }

}
