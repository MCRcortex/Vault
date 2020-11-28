package iskallia.vault.item;

import iskallia.vault.block.VaultPortalSize;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.VaultRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ItemVaultCrystal extends Item {

    private VaultRarity vaultRarity;

    public ItemVaultCrystal(ItemGroup group, ResourceLocation id, VaultRarity vaultRarity) {
        super(new Properties()
                .group(group)
                .maxStackSize(1));

        this.setRegistryName(id);
        this.vaultRarity = vaultRarity;
    }

    public static ItemStack getRandomCrystal() {
        List<Integer> weights = new ArrayList<>();
        for (int normal = 0; normal < ModConfigs.VAULT_CRYSTAL.NORMAL_WEIGHT; normal++) {
            weights.add(0);
        }
        for (int rare = 0; rare < ModConfigs.VAULT_CRYSTAL.RARE_WEIGHT; rare++) {
            weights.add(1);
        }
        for (int epic = 0; epic < ModConfigs.VAULT_CRYSTAL.EPIC_WEIGHT; epic++) {
            weights.add(2);
        }
        for (int omega = 0; omega < ModConfigs.VAULT_CRYSTAL.OMEGA_WEIGHT; omega++) {
            weights.add(3);
        }
        Random rand = new Random();
        int randomIndex = weights.get(rand.nextInt(weights.size()));
        VaultRarity vaultRarity = VaultRarity.values()[randomIndex];
        switch (vaultRarity) {
            case NORMAL:
                return new ItemStack(ModItems.VAULT_CRYSTAL_NORMAL);
            case RARE:
                return new ItemStack(ModItems.VAULT_CRYSTAL_RARE);
            case EPIC:
                return new ItemStack(ModItems.VAULT_CRYSTAL_EPIC);
            case OMEGA:
                return new ItemStack(ModItems.VAULT_CRYSTAL_OMEGA);
        }
        return new ItemStack(ModItems.VAULT_CRYSTAL_NORMAL);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getWorld().isRemote) return super.onItemUse(context);

        Item item = context.getPlayer().getHeldItemMainhand().getItem();
        if (item instanceof ItemVaultCrystal) {
            String playerBossName = "";
            if (context.getItem().hasTag()) {
                CompoundNBT tag = context.getItem().getTag();
                if (tag.keySet().contains("playerBossName")) {
                    playerBossName = tag.getString("playerBossName");
                }
            }
            ItemVaultCrystal crystal = (ItemVaultCrystal) item;
            if (tryCreatePortal(crystal, context.getWorld(), context.getPos(), context.getFace(), playerBossName)) {
                context.getItem().shrink(1);
                return ActionResultType.SUCCESS;
            }

        }
        return super.onItemUse(context);
    }

    private boolean tryCreatePortal(ItemVaultCrystal crystal, World world, BlockPos pos, Direction facing, String playerBossName) {
        Optional<VaultPortalSize> optional = VaultPortalSize.getPortalSize(world, pos.offset(facing), Direction.Axis.X);
        if (optional.isPresent()) {
            optional.get().placePortalBlocks(crystal, playerBossName);
            return true;
        }
        return false;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (stack.getItem() instanceof ItemVaultCrystal) {
            ItemVaultCrystal item = (ItemVaultCrystal) stack.getItem();
            switch (item.getRarity()) {
                case NORMAL:
                    return new StringTextComponent(item.getRarity().color + "Vault Crystal (normal)");
                case RARE:
                    return new StringTextComponent(item.getRarity().color + "Vault Crystal (rare)");
                case EPIC:
                    return new StringTextComponent(item.getRarity().color + "Vault Crystal (epic)");
                case OMEGA:
                    return new StringTextComponent(item.getRarity().color + "Vault Crystal (omega)");
            }
        }

        return super.getDisplayName(stack);
    }

    public VaultRarity getRarity() {
        return vaultRarity;
    }


}
