package djh.vesters.item;

import djh.vesters.Vesters;
import djh.vesters.item.custom.BombVestItem;
import djh.vesters.item.custom.PhoneItem;
import djh.vesters.item.custom.C4Item;
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
//    public static final Item BOMB_VEST = registerItem("bomb_vest", new BombVestItem(new FabricItemSettings().maxCount(1)));

    private static void addItemsToIngredientTabItemGroup(FabricItemGroupEntries entries){
        //commented out because these are added to a custom item group
//        entries.add(PHONE);
//        entries.add(C4);
//        entries.add(BOMB_VEST);
    }

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(Vesters.MOD_ID,name),item);
    }
    public static void registerModItems(){
        Vesters.LOGGER.info("Registering Mod Items for "+Vesters.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientTabItemGroup);
    }
}