package djh.vesters.item.custom;

import djh.vesters.entity.custom.MolotovProjectileEntity;
import djh.vesters.world.custom.MolotovExplosion;
import djh.vesters.world.custom.VestExplosion;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class MolotovItem extends Item{
    private int cooldown = 120;
    public MolotovItem(Settings settings){
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.getItemCooldownManager().set(this, cooldown);
        world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                SoundEvents.ENTITY_SPLASH_POTION_THROW,
                SoundCategory.NEUTRAL,
                0.5F,
                0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!world.isClient) {
            MolotovProjectileEntity molotovProjectileEntity = new MolotovProjectileEntity(user, world);
            molotovProjectileEntity.setItem(itemStack);
            molotovProjectileEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
            world.spawnEntity(molotovProjectileEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}