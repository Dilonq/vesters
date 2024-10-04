package djh.vesters.item.custom;

import djh.vesters.effect.ModEffects;
import djh.vesters.item.ModItems;
import djh.vesters.world.custom.VestExplosion;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class BombFoodItem extends Item{
    public BombFoodItem(Settings settings){
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.clear();
        tooltip.add(Text.translatable("item.vesters.bomb_food"));

        if (stack.hasNbt()){
            tooltip.add(Text.literal("Countdown: "+stack.getNbt().get("countdown").toString()));
        }else{
            tooltip.add(Text.literal("Set Countdown with /setcountdown"));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            if (user.getStackInHand(hand).hasNbt()){
                int countdown = user.getStackInHand(hand).getNbt().getInt("countdown") * 20; //times 20 because of ticks or whatever
                user.addStatusEffect(new StatusEffectInstance(ModEffects.EXPLOSION_COUNTDOWN, countdown,0,false,false,true));

                if (!user.getAbilities().creativeMode) {
                    user.getStackInHand(hand).decrement(1);
                    user.getItemCooldownManager().set(this, countdown);
                }
            }
        }

        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    public void call(World world, PlayerEntity user, String number){
        user.sendMessage(Text.translatable("item.vesters.phone.outgoingcall",number),false);

        for (PlayerEntity player : world.getPlayers()){
            if (true){//check if phone in inventory before checking every slot manually TODO
                for (ItemStack itemstack : player.getInventory().main){
                    if (itemstack.isOf(ModItems.PHONE) && itemstack.hasNbt()){
                        if (itemstack.getNbt().get("myNumber").asString().equals(number)){
                            user.sendMessage(Text.translatable("item.vesters.phone.ingoingcall",number),false);
                        }
                    }
                }

                if (player.getOffHandStack().isOf(ModItems.PHONE) && player.getOffHandStack().hasNbt()){
                    if (player.getOffHandStack().getNbt().get("myNumber").asString().equals(number)){
                        user.sendMessage(Text.translatable("item.vesters.phone.ingoingcall",number),false);
                    }
                }

                //check for c4 anywhere in inventory
                for (ItemStack itemstack : player.getInventory().main){
                    if (itemstack.isOf(ModItems.C4) && itemstack.hasNbt()){
                        if (itemstack.getNbt().get("myNumber").asString().equals(number)){

                            if (!user.getAbilities().creativeMode) {
                                itemstack.decrement(1);
                            }
                            explodeAt(world,player.getPos());
                        }
                    }
                }
                if (player.getOffHandStack().isOf(ModItems.C4) && player.getOffHandStack().hasNbt()){
                    if (player.getOffHandStack().getNbt().get("myNumber").asString().equals(number)){

                        if (!user.getAbilities().creativeMode) {
                            player.getInventory().removeOne(player.getOffHandStack());
                        }
                        explodeAt(world,player.getPos());
                    }
                }

                //check for bomb vest in chestplate slot
                if (player.getInventory().getArmorStack(2).isOf(ModItems.BOMB_VEST)){
                    if (player.getInventory().getArmorStack(2).hasNbt()){
                        if (player.getInventory().getArmorStack(2).getNbt().get("myNumber").asString().equals(number)){

                            if (!user.getAbilities().creativeMode) {
                                player.getInventory().removeOne(player.getInventory().getArmorStack(2));
                            }
                            explodeAt(world,player.getPos());
                        }
                    }
                }
            }

        }
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
