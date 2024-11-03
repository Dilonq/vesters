package djh.vesters.block;

import djh.vesters.Vesters;
import djh.vesters.block.custom.AdvancedBombBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block REMOTE_BOMB_BLOCK = registerBlock("remote_bomb_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));
    public static final Block ADVANCED_BOMB_BLOCK = registerBlock("advanced_bomb_block",
            new AdvancedBombBlock(FabricBlockSettings.copyOf(Blocks.TNT)));
    public static final Block BREACH_CHARGE_BLOCK = registerBlock("breach_charge_block",
            new Block(FabricBlockSettings.copyOf(Blocks.TNT)));
    private static Block registerBlock(String name, Block block){
        registerBlockItem(name,block);
        return Registry.register(Registries.BLOCK, new Identifier(Vesters.MOD_ID, name), block);
    }
    private static Item registerBlockItem(String name, Block block){
        return Registry.register(Registries.ITEM, new Identifier(Vesters.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }
    public static void registerModBlocks(){
        Vesters.LOGGER.info("Registering Mod Blocks for "+Vesters.MOD_ID);
    }
}
