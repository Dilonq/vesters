package djh.vesters;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import djh.vesters.block.ModBlocks;
import djh.vesters.item.ModItemGroups;
import djh.vesters.item.ModItems;
import djh.vesters.item.custom.PhoneItem;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.scanner.NbtCollector;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static net.minecraft.server.command.CommandManager.*;

public class Vesters implements ModInitializer {
	//primary class file for mod
	public static final String MOD_ID = "vesters";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModBlocks.registerModBlocks();
		ModItems.registerModItems();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("setdial")
				.then(argument("value", IntegerArgumentType.integer())
						.executes(context -> {

							final int value = IntegerArgumentType.getInteger(context, "value");
							if (context.getSource().getPlayerOrThrow().getMainHandStack().isOf(ModItems.PHONE)){
								if (context.getSource().getPlayerOrThrow().getMainHandStack().hasNbt()){
									NbtCompound nbtData = context.getSource().getPlayerOrThrow().getMainHandStack().getNbt();
									nbtData.putInt("toCall", value);
									context.getSource().getPlayerOrThrow().getActiveItem().setNbt(nbtData);
									context.getSource().getPlayerOrThrow().sendMessage(Text.literal("Dial # set to "+
											context.getSource().getPlayerOrThrow().getActiveItem().getNbt().get("toCall")));
								}else{
									NbtCompound nbtData = new NbtCompound();
									nbtData.putInt("toCall", value);
									context.getSource().getPlayerOrThrow().getActiveItem().setNbt(nbtData);
									context.getSource().getPlayerOrThrow().sendMessage(Text.literal("Dial # set to "+
											context.getSource().getPlayerOrThrow().getActiveItem().getNbt().get("toCall")));
								}
							}else{
								context.getSource().getPlayerOrThrow().sendMessage(Text.literal("Must be used on Phone item!"));
							}


					return 1;
				}))));

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("register")
				.executes(context -> {
					if (context.getSource().getPlayerOrThrow().getMainHandStack().isOf(ModItems.PHONE)){
						NbtCompound nbt = new NbtCompound();
						nbt.putInt("myNumber",100000+new Random().nextInt(899999));
						nbt.putInt("toCall",0);
						context.getSource().getPlayerOrThrow().getMainHandStack().setNbt(nbt);
						context.getSource().getPlayerOrThrow().sendMessage(Text.literal("Phone registered with # "+context.getSource().getPlayerOrThrow().getMainHandStack().getNbt().get("myNumber").asString()),false);
					}else if (context.getSource().getPlayerOrThrow().getMainHandStack().isOf(ModItems.C4)){
						NbtCompound nbt = new NbtCompound();
						nbt.putInt("myNumber",100000+new Random().nextInt(899999));
						context.getSource().getPlayerOrThrow().getMainHandStack().setNbt(nbt);
						context.getSource().getPlayerOrThrow().sendMessage(Text.literal("C4 registered with # "+context.getSource().getPlayerOrThrow().getMainHandStack().getNbt().get("myNumber").asString()),false);
					}else if (context.getSource().getPlayerOrThrow().getMainHandStack().isOf(ModItems.BOMB_VEST)){
						NbtCompound nbt = new NbtCompound();
						nbt.putInt("myNumber",100000+new Random().nextInt(899999));
						context.getSource().getPlayerOrThrow().getMainHandStack().setNbt(nbt);
						context.getSource().getPlayerOrThrow().sendMessage(Text.literal("Bomb Vest registered with # "+context.getSource().getPlayerOrThrow().getMainHandStack().getNbt().get("myNumber").asString()),false);
					}else{
						context.getSource().getPlayerOrThrow().sendMessage(Text.literal("Must be used on compatible item!"));
					}

					return 1;
				})));
	}
}
