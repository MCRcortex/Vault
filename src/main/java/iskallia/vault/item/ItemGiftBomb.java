package iskallia.vault.item;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

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
            }
        }

        return ActionResult.func_233538_a_(heldStack, world.isRemote());
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
