package in.techpro424.auctionhouse.economy;

import eu.pb4.common.economy.api.EconomyCurrency;
import eu.pb4.common.economy.api.EconomyProvider;
import in.techpro424.auctionhouse.AuctionHouse;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AuctionHouseCurrency implements EconomyCurrency {
    public static AuctionHouseCurrency INSTANCE = new AuctionHouseCurrency();
    public static Identifier id = new Identifier(AuctionHouse.MOD_ID, "ah-coins");

    @Override
    public Text name() {
        return Text.literal("AH Coins");
    }

    @Override
    public Identifier id() {
        return id;
    }

    @Override
    public String formatValue(long value, boolean precise) {
        return Long.toString(value);
    }

    @Override
    public long parseValue(String value) throws NumberFormatException {
        return Long.valueOf(value);
    }

    @Override
    public EconomyProvider provider() {
        return AuctionHouseProvider.INSTANCE;
    }
    
}
