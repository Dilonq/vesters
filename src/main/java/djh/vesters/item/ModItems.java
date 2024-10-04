package djh.vesters.item;

import djh.vesters.Vesters;
import djh.vesters.item.custom.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item PHONE = registerItem("phone", new PhoneItem(new FabricItemSettings().maxCount(1)));
    public static final Item C4 = registerItem("c4", new C4Item(new FabricItemSettings().maxCount(1)));
    public static final Item BOMB_VEST = registerItem("bomb_vest",new BombVestItem(ModArmorMaterials.BOMB_VEST, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item MOLOTOV = registerItem("molotov",new MolotovItem(new FabricItemSettings().maxCount(3)));
    public static final Item BOMB_FOOD = registerItem("bomb_food",new BombFoodItem(new FabricItemSettings().maxCount(1)));
    public static final Item PIPE_BOMB = registerItem("pipe_bomb",new PipeBombItem(new FabricItemSettings().maxCount(3)));

    private static void addItemsToIngredientTabItemGroup(FabricItemGroupEntries entries){
        //commented out because everything just gets added to a custom item group in the ModItemGroups class
//        entries.add(PHONE);
    }

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(Vesters.MOD_ID,name),item);
    }
    public static void registerModItems(){
        Vesters.LOGGER.info("Registering Mod Items for "+Vesters.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientTabItemGroup);
    }
}
