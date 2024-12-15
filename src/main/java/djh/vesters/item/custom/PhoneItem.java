package djh.vesters.item.custom;

import djh.vesters.item.ModItems;
import djh.vesters.world.custom.VestExplosion;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class PhoneItem extends Item{
    public PhoneItem(Settings settings){
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.clear();
        tooltip.add(Text.translatable("item.vesters.phone"));

        if (stack.hasNbt()){
            tooltip.add(Text.translatable("item.vesters.phone.mynumber",stack.getNbt().get("myNumber").toString()).formatted(Formatting.GRAY));
            if (stack.getNbt().get("toCall").toString().equals("0") || stack.getNbt().get("toCall").toString().equals("")){
                tooltip.add(Text.translatable("item.vesters.phone.emptyspeeddial").formatted(Formatting.GRAY));
            }else{
                tooltip.add(Text.translatable("item.vesters.phone.speeddial",stack.getNbt().get("toCall").toString()).formatted(Formatting.GRAY));
            }
        }else{
            tooltip.add(Text.translatable("item.vesters.phone.unregistered").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("item.vesters.phone.useregistercommand").formatted(Formatting.GRAY));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            if (user.getStackInHand(hand).hasNbt()){
                call(world,user,user.getStackInHand(hand).getNbt().get("toCall").asString());
            }else{
                NbtCompound nbt = new NbtCompound();
                nbt.putInt("myNumber",100000+new Random().nextInt(899999));
                nbt.putInt("toCall",0);
                user.getStackInHand(hand).setNbt(nbt);
                user.sendMessage(Text.translatable("item.vesters.phone.registeredwith",user.getStackInHand(hand).getNbt().get("myNumber").toString().formatted(Formatting.GRAY)),false);
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

                            // if (!user.getAbilities().creativeMode) {
                            //     itemstack.decrement(1);
                            // }
                            // explodeAt(world,player.getPos());
                        }
                    }
                }
                if (player.getOffHandStack().isOf(ModItems.C4) && player.getOffHandStack().hasNbt()){
                    if (player.getOffHandStack().getNbt().get("myNumber").asString().equals(number)){

                        // if (!user.getAbilities().creativeMode) {
                        //     player.getInventory().removeOne(player.getOffHandStack());
                        // }
                        // explodeAt(world,player.getPos());
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
