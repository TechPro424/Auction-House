package in.techpro424.auctionhouse;

import net.fabricmc.api.ModInitializer;

import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.pb4.common.economy.api.CommonEconomy;

import eu.pb4.sidebars.api.Sidebar;
import eu.pb4.sidebars.api.Sidebar.Priority;
import eu.pb4.sidebars.api.lines.SidebarLine;
import in.techpro424.auctionhouse.config.Config;
import in.techpro424.auctionhouse.config.JsonOperations;
import in.techpro424.auctionhouse.economy.AuctionHouseProvider;

import in.techpro424.auctionhouse.util.ModCommandRegister;

public class AuctionHouse implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "auction-house";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	/* Config Values */



	public static Sidebar sidebar = new Sidebar(Text.literal("§eCurrency"), Priority.LOW);

	@Override
	public void onInitialize() {

		CommonEconomy.register(MOD_ID, AuctionHouseProvider.INSTANCE);
		JsonOperations.loadConfigFromFile();
		ModCommandRegister.registerCommands();

		
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
	}

	public static void refreshSidebar() {
		sidebar.setLine(2, Text.literal(""));
		sidebar.setLine(SidebarLine.create(1, (ServerPlayerEntity) -> Text.literal(" §aBalance: " + CommonEconomy.getAccount(ServerPlayerEntity, Config.instance.getAccountId()).balance())));
		sidebar.setLine(0, Text.literal(""));

		sidebar.show();
	}
}