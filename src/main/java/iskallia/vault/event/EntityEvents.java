package iskallia.vault.event;

import iskallia.vault.world.data.VaultRaid;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityEvents {

	public static void onEntityConstruct(EntityEvent.EntityConstructing event) {
		if(!(event.getEntity() instanceof MonsterEntity) || !event.getEntity().world.isRemote)return;
		MonsterEntity entity = (MonsterEntity)event.getEntity();
		VaultRaid raid = VaultRaidData.get((ServerWorld)entity.world).getAt(entity.getPosition());
		if(raid == null)return;

		int level = raid.level;
		//Upgrade entity depending on vault level.
	}

}
