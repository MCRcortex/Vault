package iskallia.vault.item;

import iskallia.vault.client.gui.overlay.GiftBombOverlay;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModSounds;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGiftBomb extends Item {

    protected Variant variant;

    public ItemGiftBomb(ItemGroup group, Variant variant, ResourceLocation id) {
        super(new Properties()
                .group(group)
                .maxStackSize(64));

        this.variant = variant;
        this.setRegistryName(id);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        Item heldItem = heldStack.getItem();

        if (heldItem instanceof ItemGiftBomb) {
            ItemGiftBomb giftBomb = (ItemGiftBomb) heldItem;

            if (!world.isRemote) {
                ItemStack randomLoot = ModConfigs.GIFT_BOMB.randomLoot(giftBomb.variant);
                player.dropItem(randomLoot, false, false);
                heldStack.shrink(1);

                if (variant.shouldSpawnStatue()) {
                    // TODO: Drop Statue
                }

                Vector3d position = player.getPositionVec();

                world.playSound(
                        null,
                        position.x,
                        position.y,
                        position.z,
                        ModSounds.GIFT_BOMB_SFX,
                        SoundCategory.PLAYERS,
                        0.55f, 1f
                );

                ((ServerWorld) world).spawnParticle(ParticleTypes.EXPLOSION_EMITTER,
                        position.x,
                        position.y,
                        position.z,
                        3,
                        1, 1, 1,
                        0.5
                );

            } else {
                GiftBombOverlay.pop();
            }
        }

        return ActionResult.func_233538_a_(heldStack, world.isRemote());
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        IFormattableTextComponent displayName = (IFormattableTextComponent) super.getDisplayName(stack);
        displayName.setStyle(Style.EMPTY.setColor(colorForVariant(variant)));
        return displayName;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Color color = colorForVariant(variant);

        if (stack.hasTag()) {
            tooltip.add(new StringTextComponent(""));

            CompoundNBT nbt = stack.getTag();
            String gifter = nbt.getString("Gifter");
            int giftedSubs = nbt.getInt("GiftedSubs");

            tooltip.add(getPropertyInfo("Gifter", gifter, color));
            tooltip.add(getPropertyInfo("Gifted", giftedSubs + " subscribers", color));
        }

        super.addInformation(stack, world, tooltip, flagIn);
    }

    private IFormattableTextComponent getPropertyInfo(String title, String value, Color color) {
        StringTextComponent titleComponent = new StringTextComponent(title + ": ");
        titleComponent.setStyle(Style.EMPTY.setColor(color));

        StringTextComponent valueComponent = new StringTextComponent(value);
        valueComponent.setStyle(Style.EMPTY.setColor(Color.fromInt(0x00_FFFFFF)));

        return titleComponent.append(valueComponent);
    }

    private static Color colorForVariant(Variant variant) {
        if (variant == Variant.NORMAL) {
            return Color.fromInt(0x00_bc0b0b);

        } else if (variant == Variant.SUPER) {
            return Color.fromInt(0x00_9f0bbc);

        } else if (variant == Variant.MEGA) {
            return Color.fromInt(0x00_0b8fbc);

        } else if (variant == Variant.OMEGA) {
            int color = (int) System.currentTimeMillis();
            return Color.fromInt(color);
        }

        throw new InternalError("Unknown variant -> " + variant);
    }

    public static ItemStack forGift(Variant variant, String gifter, int giftedSubs) {
        ItemStack giftBomb = new ItemStack(ofVariant(variant));

        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Gifter", gifter);
        nbt.putInt("GiftedSubs", giftedSubs);

        giftBomb.setTag(nbt);

        return giftBomb;
    }

    public static Item ofVariant(Variant variant) {
        switch (variant) {
            case NORMAL:
                return ModItems.NORMAL_GIFT_BOMB;
            case SUPER:
                return ModItems.SUPER_GIFT_BOMB;
            case MEGA:
                return ModItems.MEGA_GIFT_BOMB;
            case OMEGA:
                return ModItems.OMEGA_GIFT_BOMB;
        }

        throw new InternalError("Unknown Gift Bomb variant: " + variant);
    }

    public enum Variant {
        NORMAL(false),
        SUPER(false),
        MEGA(true),
        OMEGA(true);

        boolean shouldSpawnStatue;

        Variant(boolean shouldSpawnStatue) {
            this.shouldSpawnStatue = shouldSpawnStatue;
        }

        public boolean shouldSpawnStatue() {
            return shouldSpawnStatue;
        }
    }

}
