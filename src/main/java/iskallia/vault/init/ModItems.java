package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.item.ItemSkillOrbs;
import iskallia.vault.item.ItemVaultBurger;
import iskallia.vault.item.ItemVaultCrystal;
import iskallia.vault.item.ItemVaultGem;
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
    public static ItemVaultGem VAULT_ROCK = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("vault_rock"));
    public static ItemVaultGem ALEXANDRITE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_alexandrite"));
    public static ItemVaultGem BENITOITE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_benitoite"));
    public static ItemVaultGem LARIMAR_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_larimar"));
    public static ItemVaultGem BLACK_OPAL_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_black_opal"));
    public static ItemVaultGem PAINITE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_painite"));
    public static ItemVaultGem ISKALLIUM_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_iskallium"));
    public static ItemVaultGem RENIUM_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_renium"));
    public static ItemVaultGem GORGINITE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_gorginite"));
    public static ItemVaultGem SPARKLETINE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_sparkletine"));
    public static ItemVaultGem WUTODIE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_wutodie"));
    public static ItemVaultGem POG = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_pog"));
    public static ItemVaultCrystal VAULT_CRYSTAL_NORMAL = new ItemVaultCrystal(VAULT_MOD_GROUP, Vault.id("vault_crystal_normal"), ItemVaultCrystal.CrystalRarity.NORMAL);
    public static ItemVaultCrystal VAULT_CRYSTAL_RARE = new ItemVaultCrystal(VAULT_MOD_GROUP, Vault.id("vault_crystal_rare"), ItemVaultCrystal.CrystalRarity.RARE);
    public static ItemVaultCrystal VAULT_CRYSTAL_EPIC = new ItemVaultCrystal(VAULT_MOD_GROUP, Vault.id("vault_crystal_epic"), ItemVaultCrystal.CrystalRarity.EPIC);
    public static ItemVaultCrystal VAULT_CRYSTAL_OMEGA = new ItemVaultCrystal(VAULT_MOD_GROUP, Vault.id("vault_crystal_omega"), ItemVaultCrystal.CrystalRarity.OMEGA);

    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(VAULT_BURGER);
        registry.register(SKILL_ORBS);
        registry.register(ALEXANDRITE_GEM);
        registry.register(BENITOITE_GEM);
        registry.register(LARIMAR_GEM);
        registry.register(BLACK_OPAL_GEM);
        registry.register(PAINITE_GEM);
        registry.register(ISKALLIUM_GEM);
        registry.register(RENIUM_GEM);
        registry.register(GORGINITE_GEM);
        registry.register(SPARKLETINE_GEM);
        registry.register(WUTODIE_GEM);
        registry.register(VAULT_ROCK);
        registry.register(POG);
        registry.register(VAULT_CRYSTAL_NORMAL);
        registry.register(VAULT_CRYSTAL_RARE);
        registry.register(VAULT_CRYSTAL_EPIC);
        registry.register(VAULT_CRYSTAL_OMEGA);
    }
}
