package iskallia.vault;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector implements IMixinConnector {
    @Override
    public void connect() {
        System.out.println("Mixin connector connected");
        Mixins.addConfigurations("assets/the_vault/the_vault.mixins.json");
    }
}
