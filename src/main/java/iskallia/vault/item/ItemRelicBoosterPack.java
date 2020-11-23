package iskallia.vault.item;

import iskallia.vault.init.ModConfigs;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRelicBoosterPack extends Item {

    public ItemRelicBoosterPack(ItemGroup group, ResourceLocation id) {
        super(new Properties()
                .group(group)
                .maxStackSize(64));

        this.setRegistryName(id);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote) {
            ItemStack heldStack = player.getHeldItem(hand);

            Vector3d position = player.getPositionVec();

            ((ServerWorld) world).playSound(
                    null,
                    position.x,
                    position.y,
                    position.z,
                    SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
                    SoundCategory.PLAYERS,
                    1f, 1f
            );

            ((ServerWorld) world).spawnParticle(ParticleTypes.DRAGON_BREATH,
                    position.x,
                    position.y,
                    position.z,
                    500,
                    1, 1, 1,
                    0.5
            );

            ItemVaultRelicPart randomPart = ModConfigs.VAULT_RELICS.getRandomPart();
            ItemStack itemStack = new ItemStack(randomPart);

            player.dropItem(itemStack, false, false);

            heldStack.shrink(1);
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, world, tooltip, flagIn);
    }

}
