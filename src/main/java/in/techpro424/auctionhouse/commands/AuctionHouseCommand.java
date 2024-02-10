package in.techpro424.auctionhouse.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;

import in.techpro424.auctionhouse.config.Config;
import in.techpro424.auctionhouse.gui.AuctionHouseGui;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;

public class AuctionHouseCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, RegistrationEnvironment environment) {
        LiteralCommandNode<ServerCommandSource> auctionHouseCommandNode = dispatcher.register(CommandManager.literal("auctionhouse").executes(AuctionHouseCommand::run));
        if(Config.instance.isAliasesEnabled()) dispatcher.register(CommandManager.literal("ah").redirect(auctionHouseCommandNode).executes(AuctionHouseCommand::run));
    }
    
    public static int run(CommandContext<ServerCommandSource> context) {
        try {
            AuctionHouseGui auctionHouseGui = new AuctionHouseGui(context.getSource().getPlayer());
            auctionHouseGui.open();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return 1;
    }
    
}
