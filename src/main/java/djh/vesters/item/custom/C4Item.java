package djh.vesters.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class C4Item extends Item{
    public C4Item(Settings settings){
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.clear();
        tooltip.add(Text.translatable("item.vesters.c4"));

        if (stack.hasNbt()){
            tooltip.add(Text.translatable("item.vesters.phone.mynumber",stack.getNbt().get("myNumber").toString()).formatted(Formatting.GRAY));
        }else{
            tooltip.add(Text.translatable("item.vesters.c4.unregistered").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("item.vesters.phone.useregistercommand").formatted(Formatting.GRAY));
        }
    }


    //register c4 # on right click
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            if (!user.getStackInHand(hand).hasNbt()){
                NbtCompound nbt = new NbtCompound();
                nbt.putInt("myNumber",100000+new Random().nextInt(899999));
                user.getStackInHand(hand).setNbt(nbt);
                user.sendMessage(Text.translatable("item.vesters.c4.registeredwith",user.getStackInHand(hand).getNbt().get("myNumber").toString().formatted(Formatting.GRAY)),false);
            }
        }

        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
