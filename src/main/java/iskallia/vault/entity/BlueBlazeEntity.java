package iskallia.vault.entity;

import iskallia.vault.util.EntityHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlueBlazeEntity extends BlazeEntity {

    public BlueBlazeEntity(EntityType<? extends BlazeEntity> type, World world) {
        super(type, world);
        EntityHelper.changeSize(this, 2f);
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean attackedRecently) {}

}
