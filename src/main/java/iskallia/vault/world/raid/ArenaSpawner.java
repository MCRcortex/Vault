package iskallia.vault.world.raid;

import iskallia.vault.entity.FighterEntity;
import iskallia.vault.init.ModEntities;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArenaSpawner {

	private final ArenaRaid raid;
	public final List<UUID> fighters = new ArrayList<>();
	public final List<UUID> bosses = new ArrayList<>();
	private final int bossCount;

	public ArenaSpawner(ArenaRaid raid, int bossCount) {
		this.raid = raid;
		this.bossCount = bossCount;
	}

	public void start(ServerWorld world) {
		this.fighters.clear();
		this.bosses.clear();

		for(int i = 0; i < this.bossCount; i++) {
			FighterEntity boss = ModEntities.ARENA_BOSS.create(world).changeSize(3.0F);
			BlockPos pos = this.toTop(world, this.raid.getCenter());
			boss.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.2F, pos.getZ() + 0.5F, 0.0F, 0.0F);
			this.bosses.add(boss.getUniqueID());
			world.summonEntity(boss);
		}

		int subsCount = 100;

		for(int i = 0; i < subsCount; i++) {
			double radius = 40.0D;
			double a = ((double)i / subsCount) * 2.0D * Math.PI;
			double s = Math.sin(a), c = Math.cos(a);
			double r = Math.sqrt((radius * radius) / (s * s + c * c));
			BlockPos pos = this.toTop(world, this.raid.getCenter().add(s * r, 0, c * r));

			FighterEntity fighter = ModEntities.ARENA_FIGHTER.create(world).changeSize(1.2F);
			fighter.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.2F, pos.getZ() + 0.5F, 0.0F, 0.0F);
			this.fighters.add(fighter.getUniqueID());
			world.getChunk(pos); //Force chunk loading.
			world.summonEntity(fighter);
		}
	}

	public int getFighterCount() {
		return this.fighters.size();
	}

	public BlockPos toTop(ServerWorld world, BlockPos pos) {
		return world.getHeight(Heightmap.Type.MOTION_BLOCKING, pos);
	}

}
