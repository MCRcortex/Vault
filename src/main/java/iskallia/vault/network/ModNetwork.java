package iskallia.vault.network;

import iskallia.vault.Vault;
import iskallia.vault.network.message.AbilityUpgradeMessage;
import iskallia.vault.network.message.OpenAbilityTreeMessage;
import iskallia.vault.network.message.VaultLevelMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetwork {

    private static final String NETWORK_VERSION = "0.3.0";

    public static final SimpleChannel channel = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Vault.MOD_ID, "network"),
            () -> NETWORK_VERSION,
            version -> version.equals(NETWORK_VERSION), // Client acceptance predicate
            version -> version.equals(NETWORK_VERSION) // Server acceptance predicate
    );

    public static void initialize() {
        channel.registerMessage(0, OpenAbilityTreeMessage.class, OpenAbilityTreeMessage::encode, OpenAbilityTreeMessage::decode, OpenAbilityTreeMessage::handle);
        channel.registerMessage(1, VaultLevelMessage.class, VaultLevelMessage::encode, VaultLevelMessage::decode, VaultLevelMessage::handle);
        channel.registerMessage(2, AbilityUpgradeMessage.class, AbilityUpgradeMessage::encode, AbilityUpgradeMessage::decode, AbilityUpgradeMessage::handle);
    }

}
