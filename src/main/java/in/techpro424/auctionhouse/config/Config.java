package in.techpro424.auctionhouse.config;

import in.techpro424.auctionhouse.economy.AuctionHouseAccount;
import net.minecraft.util.Identifier;

public class Config {
    long bidAmountIncrease = 50;
    Identifier accountId = AuctionHouseAccount.id;
    boolean aliasesEnabled = true;
    boolean analyticsEnabled = true;
    String server_name = "";
    int discordServerId = 1;
    int discordChannelId = 1;

    public static Config instance = JsonOperations.loadConfigFromFile();

    public long getBidAmountIncrease() {
        return Config.instance.bidAmountIncrease;
    }

    public Identifier getAccountId() {
        return Config.instance.accountId;
    }

    public boolean isAliasesEnabled() {
        return Config.instance.aliasesEnabled;
    }

    public boolean isAnalyticsEnabled() {
        return Config.instance.analyticsEnabled;
    }

    public int getDiscordServerId() {
        return Config.instance.discordServerId;
    }

    public int getDiscordChannelId() {
        return Config.instance.discordChannelId;
    }

    public String getServerName() {
        return Config.instance.server_name;
    }
}
