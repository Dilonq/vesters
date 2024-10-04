package djh.vesters.effect;

import djh.vesters.world.custom.VestExplosion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Map;

public class ExplosionCountdownEffect extends StatusEffect {
    public ExplosionCountdownEffect() {
        super(
                StatusEffectCategory.NEUTRAL,
                0x98D982);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        explodeAt(entity.getWorld(), entity.getPos());
        super.onRemoved(entity,attributes,amplifier);
    }

    public void explodeAt(World world, Vec3d pos){
        //this explosion instakills nearby entities
        VestExplosion ve = new VestExplosion(world, null, pos.getX(), pos.getY(), pos.getZ(), 4.0F, 100);
        ve.collectBlocksAndDamageEntities();
        ve.affectWorld(true);

        //this explosion sets fire and damages blocks (and damages entities but that doesnt matter because theyre already dead)
        world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 4.0F, true, World.ExplosionSourceType.TNT);


        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}
