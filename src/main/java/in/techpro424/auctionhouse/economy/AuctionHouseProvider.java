package in.techpro424.auctionhouse.economy;

import java.util.Collection;
import java.util.Collections;

import org.jetbrains.annotations.Nullable;

import com.mojang.authlib.GameProfile;

import eu.pb4.common.economy.api.EconomyAccount;
import eu.pb4.common.economy.api.EconomyCurrency;
import eu.pb4.common.economy.api.EconomyProvider;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

public class AuctionHouseProvider implements EconomyProvider {

    public static AuctionHouseProvider INSTANCE = new AuctionHouseProvider();

    @Override
    public Text name() {
        return Text.literal("Auction House");
    }

    @Override
    public @Nullable EconomyAccount getAccount(MinecraftServer server, GameProfile profile, String accountId) {
        if(!accountId.equals(AuctionHouseAccount.id.getPath())) return null;

        return new AuctionHouseAccount(profile.getId(), server);
    }

    @Override
    public Collection<EconomyAccount> getAccounts(MinecraftServer server, GameProfile profile) {
        EconomyAccount account = getAccount(server, profile, AuctionHouseAccount.id.toString());

        if (account != null) return Collections.singleton(account);
        else return Collections.emptySet();
    }

    @Override
    public @Nullable EconomyCurrency getCurrency(MinecraftServer server, String currencyId) {
        return currencyId.equals(AuctionHouseCurrency.id.toString()) ? AuctionHouseCurrency.INSTANCE : null;
    }

    @Override
    public Collection<EconomyCurrency> getCurrencies(MinecraftServer server) {
        return Collections.singleton(AuctionHouseCurrency.INSTANCE);
    }

    @Override
    public @Nullable String defaultAccount(MinecraftServer server, GameProfile profile, EconomyCurrency currency) {
        return currency == AuctionHouseCurrency.INSTANCE ? AuctionHouseCurrency.id.toString() : null;
    }
    
}
