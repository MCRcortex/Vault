package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.item.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static ItemGroup VAULT_MOD_GROUP = new ItemGroup(Vault.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(VAULT_BURGER);
        }

        @Override
        public boolean hasSearchBar() {
            return true;
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
    public static ItemVaultCrystal VAULT_CRYSTAL_NORMAL = new ItemVaultCrystal(VAULT_MOD_GROUP, Vault.id("vault_crystal_normal"), ItemVaultCrystal.Rarity.NORMAL);
    public static ItemVaultCrystal VAULT_CRYSTAL_RARE = new ItemVaultCrystal(VAULT_MOD_GROUP, Vault.id("vault_crystal_rare"), ItemVaultCrystal.Rarity.RARE);
    public static ItemVaultCrystal VAULT_CRYSTAL_EPIC = new ItemVaultCrystal(VAULT_MOD_GROUP, Vault.id("vault_crystal_epic"), ItemVaultCrystal.Rarity.EPIC);
    public static ItemVaultCrystal VAULT_CRYSTAL_OMEGA = new ItemVaultCrystal(VAULT_MOD_GROUP, Vault.id("vault_crystal_omega"), ItemVaultCrystal.Rarity.OMEGA);
    public static ItemVaultKey ALEXANDRITE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_alexandrite"));
    public static ItemVaultKey BENITOITE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_benitoite"));
    public static ItemVaultKey LARIMAR_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_larimar"));
    public static ItemVaultKey BLACK_OPAL_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_black_opal"));
    public static ItemVaultKey PAINITE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_painite"));
    public static ItemVaultKey ISKALLIUM_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_iskallium"));
    public static ItemVaultKey RENIUM_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_renium"));
    public static ItemVaultKey GORGINITE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_gorginite"));
    public static ItemVaultKey SPARKLETINE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_sparkletine"));
    public static ItemVaultKey WUTODIE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_wutodie"));
    public static ItemBit BIT_100 = new ItemBit(VAULT_MOD_GROUP, Vault.id("bit_100"), 100);
    public static ItemBit BIT_500 = new ItemBit(VAULT_MOD_GROUP, Vault.id("bit_500"), 500);
    public static ItemBit BIT_1000 = new ItemBit(VAULT_MOD_GROUP, Vault.id("bit_1000"), 1000);
    public static ItemBit BIT_5000 = new ItemBit(VAULT_MOD_GROUP, Vault.id("bit_5000"), 5000);
    public static ItemBit BIT_10000 = new ItemBit(VAULT_MOD_GROUP, Vault.id("bit_10000"), 10000);
    public static ItemRelicBoosterPack RELIC_BOOSTER_PACK = new ItemRelicBoosterPack(VAULT_MOD_GROUP, Vault.id("relic_booster_pack"));
    public static ItemVaultRelicPart DRAGON_HEAD_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_dragon_head"), "Dragon Set");
    public static ItemVaultRelicPart DRAGON_TAIL_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_dragon_tail"), "Dragon Set");
    public static ItemVaultRelicPart DRAGON_FOOT_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_dragon_foot"), "Dragon Set");
    public static ItemVaultRelicPart DRAGON_CHEST_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_dragon_chest"), "Dragon Set");
    public static ItemVaultRelicPart DRAGON_BREATH_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_dragon_breath"), "Dragon Set");
    public static ItemVaultRelicPart MINERS_DELIGHT_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_miners_delight"), "Miner's Set");
    public static ItemVaultRelicPart MINERS_LIGHT_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_miners_light"), "Miner's Set");
    public static ItemVaultRelicPart PICKAXE_HANDLE_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_pickaxe_handle"), "Miner's Set");
    public static ItemVaultRelicPart PICKAXE_HEAD_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_pickaxe_head"), "Miner's Set");
    public static ItemVaultRelicPart PICKAXE_TOOL_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_pickaxe_tool"), "Miner's Set");
    public static ItemVaultRelicPart SWORD_BLADE_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_sword_blade"), "Warror's Set");
    public static ItemVaultRelicPart SWORD_HANDLE_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_sword_handle"), "Warror's Set");
    public static ItemVaultRelicPart SWORD_STICK_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_sword_stick"), "Warror's Set");
    public static ItemVaultRelicPart WARRIORS_ARMOUR_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_warriors_armour"), "Warror's Set");
    public static ItemVaultRelicPart WARRIORS_CHARM_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_warriors_charm"), "Warror's Set");
    public static ItemVaultRelicPart DIAMOND_ESSENCE_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_diamond_essence"), "Richity Set");
    public static ItemVaultRelicPart GOLD_ESSENCE_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_gold_essence"), "Richity Set");
    public static ItemVaultRelicPart MYSTIC_GEM_ESSENCE_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_mystic_gem_essence"), "Richity Set");
    public static ItemVaultRelicPart NETHERITE_ESSENCE_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_netherite_essence"), "Richity Set");
    public static ItemVaultRelicPart PLATINUM_ESSENCE_RELIC = new ItemVaultRelicPart(VAULT_MOD_GROUP, Vault.id("relic_platinum_essence"), "Richity Set");

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
        registry.register(ALEXANDRITE_KEY);
        registry.register(BENITOITE_KEY);
        registry.register(LARIMAR_KEY);
        registry.register(BLACK_OPAL_KEY);
        registry.register(PAINITE_KEY);
        registry.register(ISKALLIUM_KEY);
        registry.register(RENIUM_KEY);
        registry.register(GORGINITE_KEY);
        registry.register(SPARKLETINE_KEY);
        registry.register(WUTODIE_KEY);
        registry.register(BIT_100);
        registry.register(BIT_500);
        registry.register(BIT_1000);
        registry.register(BIT_5000);
        registry.register(BIT_10000);
        registry.register(RELIC_BOOSTER_PACK);
        registry.register(DRAGON_HEAD_RELIC);
        registry.register(DRAGON_TAIL_RELIC);
        registry.register(DRAGON_FOOT_RELIC);
        registry.register(DRAGON_CHEST_RELIC);
        registry.register(DRAGON_BREATH_RELIC);
        registry.register(MINERS_DELIGHT_RELIC);
        registry.register(MINERS_LIGHT_RELIC);
        registry.register(PICKAXE_HANDLE_RELIC);
        registry.register(PICKAXE_HEAD_RELIC);
        registry.register(PICKAXE_TOOL_RELIC);
        registry.register(SWORD_BLADE_RELIC);
        registry.register(SWORD_HANDLE_RELIC);
        registry.register(SWORD_STICK_RELIC);
        registry.register(WARRIORS_ARMOUR_RELIC);
        registry.register(WARRIORS_CHARM_RELIC);
        registry.register(DIAMOND_ESSENCE_RELIC);
        registry.register(GOLD_ESSENCE_RELIC);
        registry.register(MYSTIC_GEM_ESSENCE_RELIC);
        registry.register(NETHERITE_ESSENCE_RELIC);
        registry.register(PLATINUM_ESSENCE_RELIC);
    }
}
