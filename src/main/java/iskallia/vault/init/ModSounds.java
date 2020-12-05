package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.util.LazySoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;

public class ModSounds {

    public static SoundEvent RAFFLE_SFX;
    public static SoundEvent VAULT_AMBIENT_LOOP;
    public static SoundEvent VAULT_AMBIENT;
    public static SoundEvent VAULT_BOSS_LOOP;
    public static SoundEvent TIMER_KILL_SFX;
    public static SoundEvent TIMER_PANIC_TICK_SFX;
    public static SoundEvent CONFETTI_SFX;
    public static SoundEvent MEGA_JUMP_SFX;
    public static SoundEvent DASH_SFX;
    public static SoundEvent VAULT_EXP_SFX;
    public static SoundEvent VAULT_LEVEL_UP_SFX;
    public static SoundEvent SKILL_TREE_LEARN_SFX;
    public static SoundEvent SKILL_TREE_UPGRADE_SFX;
    public static SoundEvent VENDING_MACHINE_SFX;
    public static SoundEvent ARENA_HORNS_SFX;
    public static SoundEvent BOOSTER_PACK_SFX;
    public static SoundEvent VAULT_GEM_HIT;
    public static SoundEvent VAULT_GEM_BREAK;

    public static LazySoundType VAULT_GEM = new LazySoundType();

    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        RAFFLE_SFX = registerSound(event, "raffle");
        VAULT_AMBIENT_LOOP = registerSound(event, "vault_ambient_loop");
        VAULT_AMBIENT = registerSound(event, "vault_ambient");
        VAULT_BOSS_LOOP = registerSound(event, "boss_loop");
        TIMER_KILL_SFX = registerSound(event, "timer_kill");
        TIMER_PANIC_TICK_SFX = registerSound(event, "timer_panic_tick");
        CONFETTI_SFX = registerSound(event, "confetti");
        MEGA_JUMP_SFX = registerSound(event, "mega_jump");
        DASH_SFX = registerSound(event, "dash");
        VAULT_EXP_SFX = registerSound(event, "vault_exp");
        VAULT_LEVEL_UP_SFX = registerSound(event, "vault_level_up");
        SKILL_TREE_LEARN_SFX = registerSound(event, "skill_tree_learn");
        SKILL_TREE_UPGRADE_SFX = registerSound(event, "skill_tree_upgrade");
        VENDING_MACHINE_SFX = registerSound(event, "vending_machine");
        ARENA_HORNS_SFX = registerSound(event, "arena_horns");
        BOOSTER_PACK_SFX = registerSound(event, "booster_pack");
        VAULT_GEM_HIT = registerSound(event, "vault_gem_hit");
        VAULT_GEM_BREAK = registerSound(event, "vault_gem_break");
    }

    public static void registerSoundTypes() {
        VAULT_GEM.initialize(0.75f, 1f, VAULT_GEM_BREAK, null, null, VAULT_GEM_HIT, null);
    }

    /* ---------------------------- */

    private static SoundEvent registerSound(RegistryEvent.Register<SoundEvent> event, String soundName) {
        ResourceLocation location = Vault.id(soundName);
        SoundEvent soundEvent = new SoundEvent(location);
        soundEvent.setRegistryName(location);
        event.getRegistry().register(soundEvent);
        return soundEvent;
    }

}
