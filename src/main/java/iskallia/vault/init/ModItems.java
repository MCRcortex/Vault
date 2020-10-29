package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.item.ItemSkillOrbs;
import iskallia.vault.item.ItemVaultBurger;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static ItemGroup VAULT_MOD_GROUP = new ItemGroup(Vault.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(SKILL_ORBS);
        }
    };

    public static ItemVaultBurger VAULT_BURGER = new ItemVaultBurger(VAULT_MOD_GROUP);
    public static ItemSkillOrbs SKILL_ORBS = new ItemSkillOrbs(VAULT_MOD_GROUP);

    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(VAULT_BURGER);
        registry.register(SKILL_ORBS);
    }

}
