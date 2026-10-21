package net.H511F1R5.cooldownstairsmod.entity.billiards;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraftforge.network.NetworkHooks;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.phys.Vec3;

public class billiardBallEntity extends Entity {

    public int lastHitGridSquare = 5;
    public boolean hitByPoolCue = false;
    public int poolCueChargeTicks = 0;

    @Override
    public boolean isPickable() {
        return true;
    }

    public void onHitEntity(EntityHitResult result) {
        onHitEntity(result, false, 0);
    }
    
    public void onHitEntity(EntityHitResult result, boolean isPoolCue, int chargeTicks) {
        this.hitByPoolCue = isPoolCue;
        this.poolCueChargeTicks = chargeTicks;
        
        Vec3 hitPoint = result.getLocation();
        
        double halfWidth = this.getBbWidth() / 2.0;
        double halfHeight = this.getBbHeight() / 2.0;
        
        if (halfWidth == 0) halfWidth = 0.0001;
        if (halfHeight == 0) halfHeight = 0.0001;
        
        Vec3 center = new Vec3(this.getX(), this.getY() + halfHeight, this.getZ());
        Vec3 offset = hitPoint.subtract(center);
        
        double relX = Math.abs(offset.x) / halfWidth;
        double relY = Math.abs(offset.y) / halfHeight;
        double relZ = Math.abs(offset.z) / halfWidth;
        
        double localX = 0;
        double localY = offset.y / halfHeight;
        
        if (relY > relX && relY > relZ) {
            localX = offset.x / halfWidth;
            localY = offset.z / halfWidth; 
        } else if (relX > relZ) {
            if (offset.x > 0) {
                localX = -offset.z / halfWidth;
            } else {
                localX = offset.z / halfWidth;
            }
        } else {
            if (offset.z > 0) {
                localX = offset.x / halfWidth;
            } else {
                localX = -offset.x / halfWidth;
            }
        }
        
        int col = 1;
        if (localX < -0.33) col = 0;
        else if (localX > 0.33) col = 2;
        
        int row = 1;
        if (localY < -0.33) row = 0;
        else if (localY > 0.33) row = 2;
        
        // 1-9 numpad representation
        // 7 8 9 (Top)
        // 4 5 6 (Middle)
        // 1 2 3 (Bottom)
        this.lastHitGridSquare = row * 3 + col + 1;
    }

    
    
    public billiardBallEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private static final EntityDataAccessor<Integer> BALL_NUM = SynchedEntityData.defineId(billiardBallEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData() {
        this.entityData.define(BALL_NUM, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("BallNum")) {
            this.setBallNum(pCompound.getInt("BallNum"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("BallNum", this.getBallNum());
    }

    public int getBallNum() {
        return this.entityData.get(BALL_NUM);
    }

    public void setBallNum(int num) {
        this.entityData.set(BALL_NUM, Math.max(0, Math.min(15, num)));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        // NetworkHooks is necessary for non-living custom entities in Forge
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
