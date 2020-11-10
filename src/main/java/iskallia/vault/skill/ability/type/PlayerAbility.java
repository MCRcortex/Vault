package iskallia.vault.skill.ability.type;

import com.google.gson.annotations.Expose;
import net.minecraft.entity.player.PlayerEntity;

public abstract class PlayerAbility {

    @Expose private int cost;
    @Expose protected Behavior behavior;

    public PlayerAbility(int cost, Behavior behavior) {
        this.cost = cost;
        this.behavior = behavior;
    }

    public int getCost() {
        return cost;
    }

    public Behavior getBehavior() {
        return behavior;
    }

    public void onAdded(PlayerEntity player) { }

    public void onFocus(PlayerEntity player) { }

    public void onBlur(PlayerEntity player) { }

    public void onTick(PlayerEntity player, boolean active) { }

    /**
     * Semantics of this dude depends on its behavior <br/>
     * - if HOLD_TO_ACTIVATE: called once holding began, released <br/>
     * - if PRESS_TO_TOGGLE: called once key is released <br/>
     * - if RELEASE_TO_PERFORM: called once key is released
     */
    public void onAction(PlayerEntity player, boolean active) { }

    public void onRemoved(PlayerEntity player) { }

    public enum Behavior {
        HOLD_TO_ACTIVATE,
        PRESS_TO_TOGGLE,
        RELEASE_TO_PERFORM;
    }

}
