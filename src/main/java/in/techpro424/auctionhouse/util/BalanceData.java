package in.techpro424.auctionhouse.util;

import net.minecraft.nbt.NbtCompound;

public class BalanceData {
    public static long addBalance(PlayerEntityDataSaverInterface player, long amount) {
        NbtCompound nbt = player.getPersistentData();
        long balance = nbt.getLong("balance");

        balance += amount;

        nbt.putLong("balance", balance);

        return balance;
    }

    public static long removeBalance(PlayerEntityDataSaverInterface player, long amount) {
        NbtCompound nbt = player.getPersistentData();
        long balance = nbt.getLong("balance");

        balance -= amount;
        if(balance < 0) balance = 0;

        nbt.putLong("balance", balance);

        return balance;
    }

    public static long setBalance(PlayerEntityDataSaverInterface player, long balance) {
        NbtCompound nbt = player.getPersistentData();
        nbt.putLong("balance", balance);
        return balance;
    }

    public static long getBalance(PlayerEntityDataSaverInterface player) {
        NbtCompound nbt = player.getPersistentData();
        return nbt.getLong("balance");

        
    }
}
