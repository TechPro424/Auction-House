package in.techpro424.auctionhouse.util;

import in.techpro424.auctionhouse.AuctionHouse;
import in.techpro424.auctionhouse.commands.AddBalanceCommand;
import in.techpro424.auctionhouse.commands.SellCommand;
import in.techpro424.auctionhouse.commands.AuctionHouseCommand;
import in.techpro424.auctionhouse.commands.RemoveBalanceCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommandRegister {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(AuctionHouseCommand::register);
        CommandRegistrationCallback.EVENT.register(SellCommand::register);
        CommandRegistrationCallback.EVENT.register(AddBalanceCommand::register);
        CommandRegistrationCallback.EVENT.register(RemoveBalanceCommand::register);
        
        AuctionHouse.LOGGER.info("Registered commands for the mod 'Auction House'");
    }
}
