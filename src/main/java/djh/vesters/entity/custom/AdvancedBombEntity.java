package djh.vesters.entity.custom;

import djh.vesters.entity.ModEntities;
import djh.vesters.item.ModItems;
import djh.vesters.world.custom.MolotovExplosion;
import net.minecraft.entity.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity.MoveEffect;
import net.minecraft.entity.data.DataTracker;
import djh.vesters.entity.ModEntities;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.World.ExplosionSourceType;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.World;
import net.minecraft.entity.TntEntity;

    public class AdvancedBombEntity extends Entity implements Ownable {
        private static final TrackedData<Integer> FUSE;
        private static final int DEFAULT_FUSE = 80;
        @Nullable
        private LivingEntity causingEntity;

        public AdvancedBombEntity(EntityType<? extends AdvancedBombEntity> entityType, World world) {
            super(entityType, world);
            this.intersectionChecked = true;
        }

        public AdvancedBombEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
            this(ModEntities.ADVANCED_BOMB_ENTITY, world);
            this.setPosition(x, y, z);
            double d = world.random.nextDouble() * 6.2831854820251465;
            this.setVelocity(-Math.sin(d) * 0.02, 0.20000000298023224, -Math.cos(d) * 0.02);
            this.setFuse(80);
            this.prevX = x;
            this.prevY = y;
            this.prevZ = z;
            this.causingEntity = igniter;
        }
        protected void initDataTracker() {
            this.dataTracker.startTracking(FUSE, 80);
        }

        protected Entity.MoveEffect getMoveEffect() {
            return MoveEffect.NONE;
        }

        public boolean canHit() {
            return !this.isRemoved();
        }

        public void tick() {
            if (!this.hasNoGravity()) {
                this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
            }

            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.98));
            if (this.isOnGround()) {
                this.setVelocity(this.getVelocity().multiply(0.7, -0.5, 0.7));
            }

            int i = this.getFuse() - 1;
            this.setFuse(i);
            if (i <= 0) {
                this.discard();
                if (!this.getWorld().isClient) {
                    this.explode();
                }
            } else {
                this.updateWaterState();
                if (this.getWorld().isClient) {
                    this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
                }
            }

        }

        private void explode() {
            float f = 4.0F;
            this.getWorld().createExplosion(this, this.getX(), this.getBodyY(0.0625), this.getZ(), 12.0F, ExplosionSourceType.TNT);
        }

        protected void writeCustomDataToNbt(NbtCompound nbt) {
            nbt.putShort("Fuse", (short) this.getFuse());
        }

        protected void readCustomDataFromNbt(NbtCompound nbt) {
            this.setFuse(nbt.getShort("Fuse"));
        }

        @Nullable
        public LivingEntity getOwner() {
            return this.causingEntity;
        }

        protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
            return 0.15F;
        }

        public void setFuse(int fuse) {
            this.dataTracker.set(FUSE, fuse);
        }

        public int getFuse() {
            return (Integer) this.dataTracker.get(FUSE);
        }

        static {
            FUSE = DataTracker.registerData(djh.vesters.entity.custom.AdvancedBombEntity.class, TrackedDataHandlerRegistry.INTEGER);
        }
    }
