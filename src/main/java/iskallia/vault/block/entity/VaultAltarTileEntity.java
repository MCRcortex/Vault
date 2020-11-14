package iskallia.vault.block.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import iskallia.vault.altar.AltarInfusion;
import iskallia.vault.altar.RequiredItem;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.VectorHelper;
import iskallia.vault.world.data.PlayerVaultAltarData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class VaultAltarTileEntity extends TileEntity implements ITickableTileEntity {

    private Map<UUID, RequiredItem[]> playerMap = new HashMap<>();
    private boolean containsVaultRock = false;

    public VaultAltarTileEntity() {
        super(ModBlocks.VAULT_ALTAR_TILE_ENTITY);
    }

    public void setContainsVaultRock(boolean containsVaultRock) {
        this.containsVaultRock = containsVaultRock;
    }

    public boolean containsVaultRock() {
        return containsVaultRock;
    }

    public void updateForClient() {
        this.world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
        this.world.notifyNeighborsOfStateChange(pos, this.getBlockState().getBlock());
        markDirty();
    }

    @Override
    public void tick() {
        World world = this.getWorld();
        if (world.isRemote)
            return;

        // do nothing if no vault rock/clear playerMap
        if (!containsVaultRock) {
            if (!playerMap.isEmpty())
                playerMap.clear();
            return;
        }
        double x = this.getPos().getX();
        double y = this.getPos().getY();
        double z = this.getPos().getZ();

        PlayerVaultAltarData data = PlayerVaultAltarData.get((ServerWorld) world);
        getNearbyPlayers(world, data, x, y, z, 30);
        pullNearbyItems(world, data, x, y, z, 8);


    }

    private void getNearbyPlayers(World world, PlayerVaultAltarData data, double x, double y, double z, int range) {

        List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, getAABB(range, x, y, z));
        for (PlayerEntity p : players) {
            if (data.playerExists(p.getUniqueID())) {
                RequiredItem[] items = data.getRequiredItems(p.getUniqueID());
                playerMap.put(p.getUniqueID(), items);
                updateForClient();
            }
        }
    }

    private void pullNearbyItems(World world, PlayerVaultAltarData data, double x, double y, double z, int range) {

        float speed = 1f / 20f; // blocks per second


        List<ItemEntity> entities = world.getEntitiesWithinAABB(ItemEntity.class, getAABB(range, x, y, z));
        for (ItemEntity itemEntity : entities) {
            for (UUID id : playerMap.keySet()) {
                RequiredItem[] items = data.getRequiredItems(id);
                for (RequiredItem required : items) {
                    if (required.getItem() == null)
                        break;

                    if (required.getItem().isItemEqualIgnoreDurability(itemEntity.getItem())) {
                        moveItemTowardPedestal(itemEntity, speed);
                        if (attemptCollectItem(itemEntity)) {
                            required.addAmount(itemEntity.getItem().getCount());
                            itemEntity.remove();
                        }
                    }
                }
            }
        }
        updateForClient();
    }

    private void moveItemTowardPedestal(ItemEntity itemEntity, float speed) {
        Vector3d target = VectorHelper.getVectorFromPos(this.getPos());
        Vector3d current = VectorHelper.getVectorFromPos(itemEntity.getPosition());

        Vector3d velocity = VectorHelper.getMovementVelocity(current, target, speed);

        itemEntity.addVelocity(velocity.x, velocity.y, velocity.z);
    }

    private boolean attemptCollectItem(ItemEntity itemEntity) {
        BlockPos itemPos = itemEntity.getPosition();

        if (itemPos.distanceSq(getPos()) <= (2 * 2)) {
            return true;
        }
        return false;
    }

    private AxisAlignedBB getAABB(int range, double x, double y, double z) {
        return new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("containsVaultRock", containsVaultRock);
        ListNBT list = new ListNBT();
        for (UUID uuid : playerMap.keySet()) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.put(uuid.toString(), AltarInfusion.serialize(playerMap.get(uuid)));
            list.add(nbt);
        }
        compound.put("players", list);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        containsVaultRock = nbt.getBoolean("containsVaultRock");
        ListNBT list = (ListNBT) nbt.get("players");
        for (INBT compound : list) {
            CompoundNBT c = (CompoundNBT) compound;
            for (String s : c.keySet()) {
                UUID id = UUID.fromString(s);
                RequiredItem[] items = AltarInfusion.deserialize((CompoundNBT) c.get(s));
                playerMap.put(id, items);
            }
        }
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        tag.putBoolean("containsVaultRock", containsVaultRock);
        ListNBT list = new ListNBT();
        for (UUID uuid : playerMap.keySet()) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.put(uuid.toString(), AltarInfusion.serialize(playerMap.get(uuid)));
            list.add(nbt);
        }
        tag.put("players", list);
        return tag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        read(state, tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getNbtCompound();
        handleUpdateTag(getBlockState(), tag);
    }

    public Map<UUID, RequiredItem[]> getNearbyPlayers() {
        return playerMap;
    }

}
