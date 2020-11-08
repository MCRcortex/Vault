package iskallia.vault.item;

import iskallia.vault.Vault;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ItemSkillOrbs extends Item {

    public ItemSkillOrbs(ItemGroup group) {
        super(new Properties()
                .group(group)
                .maxStackSize(64));

        this.setRegistryName(Vault.id("skill_orbs"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack heldItemStack = player.getHeldItem(hand);

        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(),
                SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.NEUTRAL,
                0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            PlayerAbilitiesData playerAbilitiesData = PlayerAbilitiesData.get((ServerWorld) world);
            TalentTree abilities = playerAbilitiesData.getAbilities(player);

            abilities.addSkillPoints(1);
            abilities.syncLevelInfo(world.getServer());
        }

        player.addStat(Stats.ITEM_USED.get(this));
        if (!player.abilities.isCreativeMode) {
            heldItemStack.shrink(1);
        }

        return ActionResult.func_233538_a_(heldItemStack, world.isRemote());
    }

}
