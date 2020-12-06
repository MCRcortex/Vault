package iskallia.vault.entity;

import iskallia.vault.Vault;
import iskallia.vault.world.data.ArenaRaidData;
import iskallia.vault.world.raid.ArenaRaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.HandSide;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;

import java.util.Collections;
import java.util.UUID;

public class ArenaTrackerEntity extends LivingEntity {

	public final ServerBossInfo bossInfo;

	public ArenaTrackerEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
		this.bossInfo = new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS);
		this.bossInfo.setDarkenSky(true);
		this.bossInfo.setVisible(true);

		this.setNoGravity(true);
		this.setInvulnerable(true);
		this.setInvisible(true);
	}

	@Override
	public void tick() {
		super.tick();

		if(!this.world.isRemote) {
			this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
		}
	}

	@Override
	public float getHealth() {
		if(this.world.isRemote || this.world.getDimensionKey() != Vault.ARENA_KEY)return super.getHealth();
		ArenaRaid raid = ArenaRaidData.get((ServerWorld)this.world).getAt(this.getPosition());
		if(raid == null)return super.getHealth();

		float health = 0.0F;

		for(UUID uuid: raid.spawner.fighters) {
			Entity entity = ((ServerWorld)this.world).getEntityByUuid(uuid);
			if(entity instanceof LivingEntity)health += ((LivingEntity)entity).getHealth();
		}

		return health;
	}

	@Override
	public void setCustomName(ITextComponent name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public void addTrackingPlayer(ServerPlayerEntity player) {
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(ServerPlayerEntity player) {
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
	}


	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return Collections.emptyList();
	}

	@Override
	public ItemStack getItemStackFromSlot(EquipmentSlotType slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EquipmentSlotType slot, ItemStack stack) {

	}

	@Override
	public HandSide getPrimaryHand() {
		return HandSide.LEFT;
	}

}
