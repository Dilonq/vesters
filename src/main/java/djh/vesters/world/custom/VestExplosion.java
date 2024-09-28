package djh.vesters.world.custom;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class VestExplosion extends Explosion {
    float damageMultiplier;
    World accessableWorld;
    Entity accessableEntity;
    double accessableX;
    double accessableY;
    double accessableZ;
    float accessablePower;
    List<BlockPos> accessableAffectedBlocks;

    ExplosionBehavior accessableBehavior;
    Map<PlayerEntity, Vec3d> accessableAffectedPlayers = Maps.<PlayerEntity, Vec3d>newHashMap();

    public VestExplosion(World world, @Nullable Entity entity, double x, double y, double z, float power, float damageMultiplier) {
        super(world, entity, x, y, z, power, true, Explosion.DestructionType.DESTROY_WITH_DECAY);
        this.damageMultiplier = damageMultiplier;
        this.accessableWorld = world;
        this.accessableEntity = entity;
        this.accessableX = x;
        this.accessableY = y;
        this.accessableZ = z;
        this.accessablePower = power;

        this.accessableAffectedBlocks = new ObjectArrayList<>();
        this.accessableBehavior = new ExplosionBehavior();
    }

    @Override
    public void collectBlocksAndDamageEntities() {
        this.accessableWorld.emitGameEvent(this.accessableEntity, GameEvent.EXPLODE, new Vec3d(this.accessableX, this.accessableY, this.accessableZ));
        Set<BlockPos> set = Sets.<BlockPos>newHashSet();
        int i = 16;

        for (int j = 0; j < 16; j++) {
            for (int k = 0; k < 16; k++) {
                for (int l = 0; l < 16; l++) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d = (double)((float)j / 15.0F * 2.0F - 1.0F);
                        double e = (double)((float)k / 15.0F * 2.0F - 1.0F);
                        double f = (double)((float)l / 15.0F * 2.0F - 1.0F);
                        double g = Math.sqrt(d * d + e * e + f * f);
                        d /= g;
                        e /= g;
                        f /= g;
                        float h = this.accessablePower * (0.7F + this.accessableWorld.random.nextFloat() * 0.6F);
                        double m = this.accessableX;
                        double n = this.accessableY;
                        double o = this.accessableZ;

                        for (float p = 0.3F; h > 0.0F; h -= 0.22500001F) {
                            BlockPos blockPos = BlockPos.ofFloored(m, n, o);
                            BlockState blockState = this.accessableWorld.getBlockState(blockPos);
                            FluidState fluidState = this.accessableWorld.getFluidState(blockPos);
                            if (!this.accessableWorld.isInBuildLimit(blockPos)) {
                                break;
                            }

                            Optional<Float> optional = this.accessableBehavior.getBlastResistance(this, this.accessableWorld, blockPos, blockState, fluidState);
                            if (optional.isPresent()) {
                                h -= (optional.get() + 0.3F) * 0.3F;
                            }

                            if (h > 0.0F && this.accessableBehavior.canDestroyBlock(this, this.accessableWorld, blockPos, blockState, h)) {
                                set.add(blockPos);
                            }

                            m += d * 0.3F;
                            n += e * 0.3F;
                            o += f * 0.3F;
                        }
                    }
                }
            }
        }

        this.accessableAffectedBlocks.addAll(set);
        float q = this.accessablePower * 2.0F;
        int k = MathHelper.floor(this.accessableX - (double)q - 1.0);
        int lx = MathHelper.floor(this.accessableX + (double)q + 1.0);
        int r = MathHelper.floor(this.accessableY - (double)q - 1.0);
        int s = MathHelper.floor(this.accessableY + (double)q + 1.0);
        int t = MathHelper.floor(this.accessableZ - (double)q - 1.0);
        int u = MathHelper.floor(this.accessableZ + (double)q + 1.0);
        List<Entity> list = this.accessableWorld.getOtherEntities(this.accessableEntity, new Box((double)k, (double)r, (double)t, (double)lx, (double)s, (double)u));
        Vec3d vec3d = new Vec3d(this.accessableX, this.accessableY, this.accessableZ);

        for (int v = 0; v < list.size(); v++) {
            Entity entity = (Entity)list.get(v);
            if (!entity.isImmuneToExplosion()) {
                double w = Math.sqrt(entity.squaredDistanceTo(vec3d)) / (double)q;
                if (w <= 1.0) {
                    double x = entity.getX() - this.accessableX;
                    double y = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - this.accessableY;
                    double z = entity.getZ() - this.accessableZ;
                    double aa = Math.sqrt(x * x + y * y + z * z);
                    if (aa != 0.0) {
                        x /= aa;
                        y /= aa;
                        z /= aa;
                        double ab = (double)getExposure(vec3d, entity);
                        double ac = (1.0 - w) * ab;
                        entity.damage(this.getDamageSource(), damageMultiplier * ((float)((int)((ac * ac + ac) / 2.0 * 7.0 * (double)q + 1.0))));
                        double ad;
                        if (entity instanceof LivingEntity livingEntity) {
                            ad = ProtectionEnchantment.transformExplosionKnockback(livingEntity, ac);
                        } else {
                            ad = ac;
                        }

                        x *= ad;
                        y *= ad;
                        z *= ad;
                        Vec3d vec3d2 = new Vec3d(x, y, z);
                        entity.setVelocity(entity.getVelocity().add(vec3d2));
                        if (entity instanceof PlayerEntity) {
                            PlayerEntity playerEntity = (PlayerEntity)entity;
                            if (!playerEntity.isSpectator() && (!playerEntity.isCreative() || !playerEntity.getAbilities().flying)) {
                                this.accessableAffectedPlayers.put(playerEntity, vec3d2);
                            }
                        }
                    }
                }
            }
        }
    }

}
