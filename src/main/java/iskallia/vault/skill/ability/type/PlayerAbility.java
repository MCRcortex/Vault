package iskallia.vault.skill.ability.type;

import com.google.gson.annotations.Expose;
import net.minecraft.entity.player.PlayerEntity;

public abstract class PlayerAbility {

    @Expose private int cost;

    public PlayerAbility(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public void onAdded(PlayerEntity player) { }

    public void onFocus(PlayerEntity player) { }

    public void onBlur(PlayerEntity player) { }

    public void onActivation(PlayerEntity player) { }

    public void onRemoved(PlayerEntity player) { }

}
