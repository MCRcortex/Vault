package iskallia.vault.init;

import iskallia.vault.Vault;
import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;

public class ModSounds {

    public static SoundEvent RAFFLE_SFX;
    public static SoundEvent TIMER_KILL_SFX;
    public static SoundEvent CONFETTI_SFX;
    public static SoundEvent MEGA_JUMP_SFX;
    public static SoundEvent VAULT_GEM_HIT;
    public static SoundEvent VAULT_GEM_BREAK;

    public static SoundType VAULT_GEM;

    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        RAFFLE_SFX = registerSound(event, "raffle");
        TIMER_KILL_SFX = registerSound(event, "timer_kill");
        CONFETTI_SFX = registerSound(event, "confetti");
        MEGA_JUMP_SFX = registerSound(event, "mega_jump");
        VAULT_GEM_HIT = registerSound(event, "vault_gem_hit");
        VAULT_GEM_BREAK = registerSound(event, "vault_gem_break");
    }

    public static void registerSoundTypes() {
        VAULT_GEM = new SoundType(1f, 1f, VAULT_GEM_BREAK, VAULT_GEM_HIT, VAULT_GEM_HIT, VAULT_GEM_HIT, VAULT_GEM_HIT);
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
