package iskallia.vault.init;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;

public class ModSounds {

    public static SoundEvent RAFFLE_SFX;
    public static SoundEvent TIMER_KILL_SFX;

    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        RAFFLE_SFX = registerSound(event, "raffle");
        TIMER_KILL_SFX = registerSound(event, "timer_kill");
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
