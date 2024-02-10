package in.techpro424.auctionhouse.gui;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import eu.pb4.common.economy.api.CommonEconomy;
import eu.pb4.common.economy.api.EconomyTransaction;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;

import in.techpro424.auctionhouse.analytics.SaleItem;
import in.techpro424.auctionhouse.config.Config;
import in.techpro424.auctionhouse.storage.ServerState;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class AuctionItemGui extends SimpleGui {

    public AuctionItemGui(ServerPlayerEntity player, int itemIndex) {
        super(ScreenHandlerType.GENERIC_9X6, player, true);
        this.setLockPlayerInventory(true);

        ServerState serverState = ServerState.getServerState(player.getServer());
        ItemStack stack = serverState.getAuctionList().get(itemIndex);

        this.setTitle(stack.getName());

        

        this.setSlot(22, GuiElementBuilder.from(stack)
        .addLoreLine(Text.of(""))
        .addLoreLine(Text.of("Owner: " + player.getServer().getPlayerManager()
            .getPlayer(stack.getSubNbt("auction-data").getUuid("ownerUUID")).getName().getString())));
        

        this.setSlot(31, new GuiElementBuilder(Items.LIME_CONCRETE, 1)
        .setName(Text.of("Increase bid"))
        .addLoreLine(Text.of(""))
        .addLoreLine(Text.of("Staring Price: " + stack.getSubNbt("auction-data").getLong("starting-price")))
        .addLoreLine(Text.of(""))
        .addLoreLine(Text.of("Current Highest Bidder: " + player.getServer().getPlayerManager().getPlayer(stack.getSubNbt("auction-data").getUuid("highest-bidder-UUID")).getName().getString()))
        .addLoreLine(Text.of("Current Bidding Price: " + stack.getSubNbt("auction-data").getLong("current-bidding-price")))
        .setCallback((index, clickType, slotActionType) -> {
            long cbp = stack.getSubNbt("auction-data").getLong("current-bidding-price");
            stack.getSubNbt("auction-data").putLong("current-bidding-price", cbp + Config.instance.getBidAmountIncrease());
            stack.getSubNbt("auction-data").putUuid("highest-bidder-UUID", player.getUuid());
            serverState.markDirty();

            AuctionItemGui auctionItemGui = new AuctionItemGui(player, itemIndex);
            auctionItemGui.open();

        })
        .build());

        this.setSlot(45, new GuiElementBuilder(Items.BLUE_CONCRETE, 1)
        .setName(Text.of("Back to the Auction House"))
        .setCallback((index, clickType, slotActionType) -> {
            AuctionHouseGui auctionHouseGui = new AuctionHouseGui(player);
            this.close();
            auctionHouseGui.open();
        })
        .build());

        this.setSlot(49, new GuiElementBuilder(Items.YELLOW_CONCRETE, 1)
        .setName(Text.of("Sell to the highest bidder"))
        .addLoreLine(Text.of(""))
        .addLoreLine(Text.of("Click to sell this item to the highest bidder"))
        .addLoreLine(Text.of("(only works if you are the owner of this item)"))
        .setCallback((index, clickType, slotActionType) -> {

            String selling_time = ZonedDateTime.now(ZoneId.of("UTC")).toString();

            UUID ownerUUID = stack.getSubNbt("auction-data").getUuid("ownerUUID");

            if(ownerUUID.equals(player.getUuid())) {
                try {
                    ServerPlayerEntity buyer = player.getServer().getPlayerManager().getPlayer(stack.getSubNbt("auction-data").getUuid("highest-bidder-UUID"));
                    if(buyer.getUuid().equals(ownerUUID)) {
                        buyer.sendMessage(Text.literal("§cThe seller cannot buy the item."));
                        this.close();
                        return;
                    }
                    long price = stack.getSubNbt("auction-data").getLong("current-bidding-price");

                    EconomyTransaction transaction = CommonEconomy.getAccount(buyer, Config.instance.getAccountId()).decreaseBalance(price);
                    if(transaction.isFailure()) {
                        buyer.sendMessage(transaction.message());
                        this.close();
                        return;
                    }

                    SaleItem.getFromStack(stack, selling_time).send();

                    serverState.getAuctionList().remove(itemIndex);
                    serverState.markDirty();

                    stack.removeSubNbt("auction-data");

                    CommonEconomy.getAccount(player, Config.instance.getAccountId()).increaseBalance(price);
                    
                    buyer.giveItemStack(stack);

                    AuctionHouseGui auctionHouseGui = new AuctionHouseGui(player);
                    this.close();
                    auctionHouseGui.open();
                    

                    

                    

                } catch(Exception e) {
                    e.printStackTrace();
                }
    
               
            }
            else {
                player.sendMessage(Text.of("You do not own this item."));
            }
        })
        .build());

        this.setSlot(53, new GuiElementBuilder(Items.RED_CONCRETE, 1)
        .setName(Text.of("Remove Item"))
        .addLoreLine(Text.of(""))
        .addLoreLine(Text.of("Click to remove this item from the Auction House"))
        .addLoreLine(Text.of("(only works if you are the owner of this item)"))
        .setCallback((index, clickType, slotActionType) -> {
            UUID ownerUUID = stack.getSubNbt("auction-data").getUuid("ownerUUID");

            if(ownerUUID.equals(player.getUuid())) {
                try {
                    serverState.getAuctionList().remove(itemIndex);
                    serverState.markDirty();
    
                    stack.removeSubNbt("auction-data");
                    
                    player.giveItemStack(stack);

                    AuctionHouseGui auctionHouseGui = new AuctionHouseGui(player);
                    this.close();
                    auctionHouseGui.open();

                } catch(Exception e) {
                    e.printStackTrace();
                }
    
               
            }
            else {
                player.sendMessage(Text.of("You do not own this item."));
            }
        })
        .build());
    }
    
}
