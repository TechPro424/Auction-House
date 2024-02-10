package in.techpro424.auctionhouse.analytics;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import com.google.gson.GsonBuilder;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import in.techpro424.auctionhouse.AuctionHouse;
import in.techpro424.auctionhouse.config.Config;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

public class SaleItem {
    String server_name;
    long starting_price;
    long selling_price;
    String item_name;
    int max_durability;
    int lost_durability;
    ArrayList<String> enchantments;
    String timeOfAuction;
    String timeOfSelling;
    UUID uuid;
    int discordServerId;
    

    public SaleItem(long start_price, long sell_price, String item_name, int max_durability, int lost_durability, 
    ArrayList<String> enchantments, String timeOfAuction, String timeOfSelling) {
        this.starting_price = start_price;
        this.selling_price = sell_price;
        this.item_name = item_name;
        this.max_durability = max_durability;
        this.lost_durability = lost_durability;
        this.enchantments = enchantments;
        this.timeOfAuction = timeOfAuction;
        this.timeOfSelling = timeOfSelling;
        this.uuid = UUID.randomUUID();
        this.discordServerId = Config.instance.getDiscordServerId();
    }

    public void send() throws ClientProtocolException, IOException {
        AuctionHouse.LOGGER.info(Boolean.toString(Config.instance.isAnalyticsEnabled()));
        if(!Config.instance.isAnalyticsEnabled()) return;

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://auction-house-api.onrender.com/view/" + Config.instance.getServerName() + "/");

        StringEntity postString = new StringEntity(this.toJsonObject());
        AuctionHouse.LOGGER.info(this.toJsonObject());

        post.setEntity(postString);
        post.setHeader("Content-type", "application/json");

        client.execute(post);
        //send the sale item
    }

    public static SaleItem getFromStack(ItemStack stack, String selling_time) {
        long starting_price = stack.getSubNbt("auction-data").getLong("starting-price");
        long selling_price = stack.getSubNbt("auction-data").getLong("current-bidding-price");
        String timeOfAuction = stack.getSubNbt("auction-data").getString("time-of-auction");

        Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.get(stack);
        ArrayList<String> enchantmentNames = new ArrayList<String>();

        for (Enchantment e: enchantmentMap.keySet()) {
            enchantmentNames.add(e.getName(EnchantmentHelper.getLevel(e, stack)).getString());
        }

        return new SaleItem(starting_price, selling_price, Registries.ITEM.getId(stack.getItem()).toString(), 
        stack.getMaxDamage(), stack.getDamage(), enchantmentNames, timeOfAuction, selling_time);
    }

    public String toJsonObject() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
