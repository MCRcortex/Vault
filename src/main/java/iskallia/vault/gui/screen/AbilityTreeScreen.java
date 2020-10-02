package iskallia.vault.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.ability.AbilityTree;
import iskallia.vault.container.AbilityTreeContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AbilityTreeScreen extends ContainerScreen<AbilityTreeContainer> {

    public AbilityTreeScreen(AbilityTreeContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, new StringTextComponent("Ability Tree Screen!"));
    }

    @Override
    protected void
    drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        System.out.println("Epic rendering routine of Ability Tree continues");
        // getContainer().getAbilityTree().getNodes() <-- This is how you access the model
    }

}
