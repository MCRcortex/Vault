package iskallia.vault.mixin;

import net.minecraft.client.gui.screen.inventory.AnvilScreen;
import net.minecraft.inventory.container.RepairContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(RepairContainer.class)
public class MixinRepairContainer {
    @ModifyConstant(method = "updateRepairOutput", constant = @Constant(intValue = 40, ordinal = 2))
    private int overrideMaxRepairLevel(int oldValue) {
        return Integer.MAX_VALUE;
    }
}
