package iskallia.vault.item;

import iskallia.vault.Vault;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ItemVaultBurger extends Item {

    public static Food VAULT_BURGER_FOOD = new Food.Builder()
            .saturation(0).hunger(0)
            .fastToEat().setAlwaysEdible().build();

    public ItemVaultBurger(ItemGroup group) {
        super(new Properties()
                .group(group)
                .food(VAULT_BURGER_FOOD)
                .maxStackSize(64));

        this.setRegistryName(Vault.id("vault_burger"));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entityLiving) {
        if (!world.isRemote) {
            ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;

            PlayerVaultStatsData statsData = PlayerVaultStatsData.get((ServerWorld) world);

            PlayerVaultStats stats = statsData.getVaultStats(player);

            float randomPercentage = MathUtilities.randomFloat(
                    ModConfigs.VAULT_ITEMS.VAULT_BURGER.minExpPercent,
                    ModConfigs.VAULT_ITEMS.VAULT_BURGER.maxExpPercent);

            statsData.addVaultExp(player, (int) (stats.getTnl() * randomPercentage));
        }

        return super.onItemUseFinish(stack, world, entityLiving);
    }

}
