package in.techpro424.auctionhouse.economy;

import java.util.UUID;

import eu.pb4.common.economy.api.EconomyAccount;
import eu.pb4.common.economy.api.EconomyCurrency;
import eu.pb4.common.economy.api.EconomyProvider;
import eu.pb4.common.economy.api.EconomyTransaction;

import in.techpro424.auctionhouse.AuctionHouse;
import in.techpro424.auctionhouse.util.BalanceData;
import in.techpro424.auctionhouse.util.PlayerEntityDataSaverInterface;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AuctionHouseAccount implements EconomyAccount {

    public UUID ownerUuid;
    public MinecraftServer server;
    public ServerPlayerEntity player;
    public static Identifier id = new Identifier(AuctionHouse.MOD_ID, "auction-house-account");

    public AuctionHouseAccount(UUID ownerUuid, MinecraftServer server) {
        this.ownerUuid = ownerUuid;
        this.server = server;

        try {
            this.player = server.getPlayerManager().getPlayer(ownerUuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    

    @Override
    public Text name() {
        return Text.of(player.getName().getString() + "'s account");
    }

    @Override
    public UUID owner() {
        return ownerUuid;
    }

    @Override
    public Identifier id() {
        return id;
    }

    @Override
    public EconomyTransaction canIncreaseBalance(long value) {
        long current = balance();

        return new EconomyTransaction.Simple(
            true,
            Text.literal("Added " + value + " to " + player.getName().getString() + "'s account"),
            current + value,
            current,
            value,
            this
        );
    }

    

    @Override
    public EconomyTransaction canDecreaseBalance(long value) {
        long current = balance();

        if (current < value) {
            return new EconomyTransaction.Simple(
                false,
                Text.literal(player.getName().getString() + " doesn't have enough money! (" + current + " < " + value + ")"),
                current,
                current,
                0,
                this
            );
        } else {
            return new EconomyTransaction.Simple(
                true,
                Text.literal("Removed " + value + " from " + player.getName().getString() + "'s account"),
                current - value,
                current,
                value,
                this
            );
        }
    }

    @Override
    public EconomyProvider provider() {
        return AuctionHouseProvider.INSTANCE;
    }

    @Override
    public EconomyCurrency currency() {
        return AuctionHouseCurrency.INSTANCE;
    }

    @Override
    public long balance() {
        return BalanceData.getBalance(((PlayerEntityDataSaverInterface)player));
    }



    @Override
    public void setBalance(long value) {
        AuctionHouse.LOGGER.info("setBalance was called");

        BalanceData.setBalance(((PlayerEntityDataSaverInterface)player), value); 

        AuctionHouse.refreshSidebar();
    }
    
}
