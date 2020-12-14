package iskallia.vault.item;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModSounds;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
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
            float rand = world.rand.nextFloat() * 100;
            ItemStack heldStack = player.getHeldItem(hand);
            ItemStack stackToDrop = null;

            if (rand >= 98) {
                ItemVaultRelicPart randomPart = ModConfigs.VAULT_RELICS.getRandomPart();
                stackToDrop = new ItemStack(randomPart);
                successEffects(world, player.getPositionVec());

            } else if (rand >= 96) {
                stackToDrop = new ItemStack(ModItems.SKILL_ORB);
                successEffects(world, player.getPositionVec());

            } else {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                ServerWorld serverWorld = serverPlayer.getServerWorld();
                int expPerSub = ModConfigs.STREAMER_EXP.getExpPerSub(player.getName().getString());
                float coef = MathUtilities.randomFloat(0.5f, 1f);
                PlayerVaultStatsData.get(serverWorld).addVaultExp(serverPlayer, (int) (expPerSub * coef));
                failureEffects(world, player.getPositionVec());
            }

            if (stackToDrop != null)
                player.dropItem(stackToDrop, false, false);
            heldStack.shrink(1);
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, world, tooltip, flagIn);
    }

    public static void successEffects(World world, Vector3d position) {
        world.playSound(
                null,
                position.x,
                position.y,
                position.z,
                ModSounds.BOOSTER_PACK_SUCCESS_SFX,
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
    }

    public static void failureEffects(World world, Vector3d position) {
        world.playSound(
                null,
                position.x,
                position.y,
                position.z,
                ModSounds.BOOSTER_PACK_FAIL_SFX,
                SoundCategory.PLAYERS,
                1f, 1f
        );

        ((ServerWorld) world).spawnParticle(ParticleTypes.SMOKE,
                position.x,
                position.y,
                position.z,
                500,
                1, 1, 1,
                0.5
        );
    }

}
