package djh.vesters.entity.custom;

import djh.vesters.entity.ModEntities;
import djh.vesters.item.ModItems;
import djh.vesters.world.custom.PipeBombExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PipeBombProjectileEntity extends ThrownItemEntity {

    public PipeBombProjectileEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public PipeBombProjectileEntity(LivingEntity livingEntity, World world) {
        super(ModEntities.PBOMB_PROJECTILE, livingEntity, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.PIPE_BOMB;
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.SMOKE;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket(){
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        this.getWorld()
                .addParticle(
                        ParticleTypes.SMOKE,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        this.random.nextGaussian() * 0.00,
                        -this.getVelocity().y * 0.0,
                        this.random.nextGaussian() * 0.00
                );
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult){
        if (!this.getWorld().isClient()){
            this.getWorld().sendEntityStatus(this, (byte)3);
            explodeAt(this.getWorld(), blockHitResult.getPos());
//            this.getWorld().setBlockState(getBlockPos(), )
        }

        this.discard();
        super.onBlockHit(blockHitResult);
    }

    public void explodeAt(World world, Vec3d pos){
        //just set fire, don't damage entities or blocks
        PipeBombExplosion explosion = new PipeBombExplosion(world, null, pos.getX(), pos.getY(), pos.getZ(), 4.0f);
        explosion.collectBlocksAndDamageEntities();
        explosion.affectWorld(true);

        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.BLOCKS, 1.0F, 1.1F);
    }
}
