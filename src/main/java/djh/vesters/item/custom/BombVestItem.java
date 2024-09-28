package djh.vesters.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BombVestItem extends ArmorItem {
    public BombVestItem(ArmorMaterial material, Type type, Settings settings){
        super(material, type, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.clear();
        tooltip.add(Text.translatable("item.vesters.bomb_vest"));

        if (stack.hasNbt() && stack.getNbt().contains("myNumber")){
            tooltip.add(Text.translatable("item.vesters.phone.mynumber",stack.getNbt().get("myNumber").toString()).formatted(Formatting.GRAY));
        }else{
            tooltip.add(Text.translatable("item.vesters.bomb_vest.unregistered").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("item.vesters.phone.useregistercommand").formatted(Formatting.GRAY));
        }
    }
}
