package in.techpro424.auctionhouse.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import eu.pb4.common.economy.api.CommonEconomy;
import eu.pb4.common.economy.api.EconomyTransaction;

import in.techpro424.auctionhouse.AuctionHouse;
import in.techpro424.auctionhouse.config.Config;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.network.ServerPlayerEntity;

public class AddBalanceCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("addbalance")
        .requires(source -> source.hasPermissionLevel(4))
        .then(CommandManager.argument("player", EntityArgumentType.player())
        .then(CommandManager.argument("price", LongArgumentType.longArg(0, (Long.MAX_VALUE - 1)))
        .executes(AddBalanceCommand::run))));
    }
    
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
        long price = LongArgumentType.getLong(context, "price");

        EconomyTransaction transaction = CommonEconomy.getAccount(player, Config.instance.getAccountId()).increaseBalance(price);
        context.getSource().getPlayer().sendMessage(transaction.message());

        AuctionHouse.sidebar.markDirty();
        AuctionHouse.sidebar.show();
        return 1;
    }
}
