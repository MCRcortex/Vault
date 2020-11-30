package iskallia.vault.util;

import net.minecraft.block.SoundType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

// Y E S
public class LazySoundType extends SoundType {

    private boolean initialized;
    protected float lazyVolume;
    protected float lazyPitch;
    protected SoundEvent lazyBreakSound;
    protected SoundEvent lazyStepSound;
    protected SoundEvent lazyPlaceSound;
    protected SoundEvent lazyHitSound;
    protected SoundEvent lazyFallSound;

    public LazySoundType() {
        super(1f, 1f, SoundEvents.BLOCK_STONE_BREAK, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);
    }

    public void initialize(float volumeIn, float pitchIn, SoundEvent breakSoundIn, SoundEvent stepSoundIn, SoundEvent placeSoundIn, SoundEvent hitSoundIn, SoundEvent fallSoundIn) {
        if (initialized) throw new InternalError("LazySoundTypes should be initialized only once!");
        this.lazyVolume = volumeIn;
        this.lazyPitch = pitchIn;
        this.lazyBreakSound = breakSoundIn;
        this.lazyStepSound = stepSoundIn;
        this.lazyPlaceSound = placeSoundIn;
        this.lazyHitSound = hitSoundIn;
        this.lazyFallSound = fallSoundIn;
        initialized = true;
    }

    @Override
    public float getVolume() {
        return lazyVolume;
    }

    @Override
    public float getPitch() {
        return lazyPitch;
    }

    @Override
    public SoundEvent getBreakSound() {
        return lazyBreakSound;
    }

    @Override
    public SoundEvent getStepSound() {
        return lazyStepSound;
    }

    @Override
    public SoundEvent getPlaceSound() {
        return lazyPlaceSound;
    }

    @Override
    public SoundEvent getHitSound() {
        return lazyHitSound;
    }

    @Override
    public SoundEvent getFallSound() {
        return lazyFallSound;
    }

}
