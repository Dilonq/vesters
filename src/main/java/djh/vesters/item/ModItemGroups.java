package djh.vesters.item;

import djh.vesters.Vesters;
import djh.vesters.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup VESTERS_GROUP = Registry.register(Registries.ITEM_GROUP,
        new Identifier(Vesters.MOD_ID,"vesters"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.vesters"))
                    .icon(() -> new ItemStack(ModItems.PHONE)).entries((displayContext, entries) -> {
                        entries.add(ModItems.PHONE);
                        entries.add(ModItems.C4);
                        entries.add(ModItems.BOMB_VEST);
                        entries.add(ModItems.MOLOTOV);
                        entries.add(ModItems.BOMB_FOOD);
                        entries.add(ModItems.PIPE_BOMB);
                        entries.add(ModBlocks.REMOTE_BOMB_BLOCK);
                        entries.add(ModBlocks.BREACH_CHARGE_BLOCK);
                        entries.add(ModBlocks.ENDITE_NUKE);
                        entries.add(ModBlocks.ADVANCED_BOMB_BLOCK);
                        entries.add(ModItems.CONDENSED_GUNPOWDER);
                        entries.add(ModItems.WIRE_CUTTERS);
                    }).build());
    public static void registerItemGroups(){
        Vesters.LOGGER.info("Registering Item Groups for "+Vesters.MOD_ID);


    }
}
