package in.techpro424.auctionhouse.gui;

import java.util.ArrayList;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;

import in.techpro424.auctionhouse.storage.ServerState;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class AuctionHouseGui extends SimpleGui {

    public AuctionHouseGui(ServerPlayerEntity player) {
        super(ScreenHandlerType.GENERIC_9X6, player, true);
        this.setLockPlayerInventory(true);
        this.setTitle(Text.of("Auction House"));
        
        ServerState serverState = ServerState.getServerState(player.getServer());
        ArrayList<ItemStack> list = serverState.getAuctionList();
        
        for(ItemStack stack : list ) {
                this.setSlot(list.indexOf(stack), GuiElementBuilder.from(stack)

                .addLoreLine(Text.of(""))

                .addLoreLine(Text.of("Owner: " + player.getServer().getPlayerManager().getPlayer(stack.getSubNbt("auction-data").getUuid("ownerUUID")).getName().getString()))
                .addLoreLine(Text.of("Starting Price: " + stack.getSubNbt("auction-data").getLong("starting-price")))
                .addLoreLine(Text.of(""))
                .addLoreLine(Text.of("Current Highest Bidder: " + player.getServer().getPlayerManager().getPlayer(stack.getSubNbt("auction-data").getUuid("highest-bidder-UUID")).getName().getString()))
                .addLoreLine(Text.of("Current Bidding Price: " + stack.getSubNbt("auction-data").getLong("current-bidding-price")))
                
                .setCallback((index, clickType, slotActionType) -> {
                    AuctionItemGui auctionItemGui = new AuctionItemGui(player, list.indexOf(stack));
                    auctionItemGui.open();
            }));
            
            
        }
        
    }
    
}
