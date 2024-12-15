package djh.vesters;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import djh.vesters.block.ModBlocks;
import djh.vesters.effect.ModEffects;
import djh.vesters.entity.custom.AdvancedBombEntity;
import djh.vesters.item.ModItemGroups;
import djh.vesters.item.ModItems;
import djh.vesters.item.custom.PhoneItem;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.TntEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.scanner.NbtCollector;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ModStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static djh.vesters.item.ModItems.WIRE_CUTTERS;
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
		ModEffects.registerModEffects();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("setdial")
				.then(argument("value", IntegerArgumentType.integer())
						.executes(context -> {

							final int value = IntegerArgumentType.getInteger(context, "value");
							if (context.getSource().getPlayerOrThrow().getMainHandStack().isOf(ModItems.PHONE)){
								if (context.getSource().getPlayerOrThrow().getMainHandStack().hasNbt()){
									NbtCompound nbtData = context.getSource().getPlayerOrThrow().getMainHandStack().getNbt();
									nbtData.putInt("toCall", value);
									context.getSource().getPlayerOrThrow().getMainHandStack().setNbt(nbtData);
									context.getSource().getPlayerOrThrow().sendMessage(Text.literal("Dial # set to "+
											context.getSource().getPlayerOrThrow().getActiveItem().getNbt().get("toCall")));
								}else{
									NbtCompound nbtData = new NbtCompound();
									nbtData.putInt("toCall", value);
									context.getSource().getPlayerOrThrow().getMainHandStack().setNbt(nbtData);
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

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("setcountdown")
				.then(argument("value", IntegerArgumentType.integer())
						.executes(context -> {

							final int value = IntegerArgumentType.getInteger(context, "value");
							if (context.getSource().getPlayerOrThrow().getMainHandStack().isOf(ModItems.BOMB_FOOD) && value>=60){
									NbtCompound nbtData = new NbtCompound();
									nbtData.putInt("countdown", value);
									context.getSource().getPlayerOrThrow().getMainHandStack().setNbt(nbtData);
									context.getSource().getPlayerOrThrow().sendMessage(Text.literal("Countdown set"));
							}else{
								context.getSource().getPlayerOrThrow().sendMessage(Text.literal("Must be >=60 and used on applicable item!"));
							}


							return 1;
						}))));
			// Register the interaction event
			UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
				if (!world.isClient && hand == Hand.MAIN_HAND) {
					// Check if the player is holding the wire cutters and interacting with TNT
					if (player.getStackInHand(hand).getItem() == WIRE_CUTTERS && entity instanceof TntEntity) {
						entity.kill(); // Kill the TNT entity
						player.getStackInHand(hand).damage(1, player, (p) -> p.sendToolBreakStatus(hand)); // Damage the item

						world.playSound(  null, // Target player, null means all nearby players hear it
								entity.getBlockPos(), // Position of the sound
								SoundEvents.ENTITY_SHEEP_SHEAR, // The shear sound effect
								SoundCategory.PLAYERS, // The sound category
								1.0F, // Volume
								1.0F);  // Pitch);
						return ActionResult.SUCCESS; // Indicate the interaction was successful
					}
				}
				return ActionResult.PASS; // Allow other interactions to occur
			});
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (!world.isClient && hand == Hand.MAIN_HAND) {
				// Check if the player is holding the wire cutters and interacting with TNT
				if (player.getStackInHand(hand).getItem() == WIRE_CUTTERS && entity instanceof AdvancedBombEntity) {
					entity.kill(); // Kill the TNT entity
					player.getStackInHand(hand).damage(1, player, (p) -> p.sendToolBreakStatus(hand)); // Damage the item

					world.playSound(  null, // Target player, null means all nearby players hear it
							entity.getBlockPos(), // Position of the sound
							SoundEvents.ENTITY_SHEEP_SHEAR, // The shear sound effect
							SoundCategory.PLAYERS, // The sound category
							1.0F, // Volume
							1.0F);  // Pitch);
					return ActionResult.SUCCESS; // Indicate the interaction was successful
				}
			}
			return ActionResult.PASS; // Allow other interactions to occur
		});
	}
}
