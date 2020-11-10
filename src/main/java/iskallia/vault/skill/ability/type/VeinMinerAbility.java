package iskallia.vault.skill.ability.type;

import com.google.gson.annotations.Expose;

public class VeinMinerAbility extends PlayerAbility {

    @Expose private final int blockLimit;

    public VeinMinerAbility(int cost, int blockLimit) {
        super(cost, Behavior.HOLD_TO_ACTIVATE);
        this.blockLimit = blockLimit;
    }

    public int getBlockLimit() {
        return blockLimit;
    }

}
