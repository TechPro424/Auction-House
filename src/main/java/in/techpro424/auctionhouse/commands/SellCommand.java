package in.techpro424.auctionhouse.commands;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import in.techpro424.auctionhouse.storage.ServerState;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;

import net.minecraft.command.CommandRegistryAccess;

import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SellCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("sell")
        .then(CommandManager.argument("quantity", IntegerArgumentType.integer(1, (Integer.MAX_VALUE - 1)))
        .then(CommandManager.argument("price", LongArgumentType.longArg(0, (Long.MAX_VALUE - 1)))
        .executes(SellCommand::run))));
    }
    
    public static int run(CommandContext<ServerCommandSource> context) {

        try {

        

        ZonedDateTime timeOfAuction = ZonedDateTime.now(ZoneId.of("UTC"));

        ServerPlayerEntity player = context.getSource().getPlayer();
        
        if(player.getMainHandStack().isEmpty()) return 1;

        int quantity = IntegerArgumentType.getInteger(context, "quantity");

        if(quantity > player.getMainHandStack().getCount()) {
            context.getSource().sendFeedback(() -> Text.literal("§cYou do not have enough items"), false);
            return 1;
        }

        ItemStack stack = player.getMainHandStack().split(quantity);
        long price = LongArgumentType.getLong(context, "price");

        stack.getOrCreateSubNbt("auction-data").putUuid("ownerUUID", player.getUuid());
        stack.getOrCreateSubNbt("auction-data").putLong("starting-price", price);
        stack.getOrCreateSubNbt("auction-data").putLong("current-bidding-price", price);
        stack.getOrCreateSubNbt("auction-data").putUuid("highest-bidder-UUID", player.getUuid());
        stack.getOrCreateSubNbt("auction-data").putString("time-of-auction", timeOfAuction.toString());
        
        
        ServerState serverState = ServerState.getServerState(player.getServer());
        serverState.getAuctionList().add(stack);
        serverState.markDirty();
    } catch (Exception e) {
        e.printStackTrace();
    }
        return 1;
    }
    
}
