package iskallia.vault.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class MonsterEyeEntity extends SlimeEntity {

    public MonsterEyeEntity(EntityType<? extends SlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean attackedRecently) { }

}
