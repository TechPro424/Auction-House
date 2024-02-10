package in.techpro424.auctionhouse.storage;

import java.util.ArrayList;

import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;


public class ServerState extends PersistentState {

    public ArrayList<ItemStack> auctionList = new ArrayList<ItemStack>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.put("auction-items", convertArrayListToNbtList(auctionList));
        return nbt;
    }

    public static ServerState fromNbt(NbtCompound tag) {
        ServerState serverState = new ServerState();
        serverState.auctionList = convertNbtListToArrayList(tag.getList("auction-items", NbtCompound.COMPOUND_TYPE));
        return serverState;
    }

    static Type<ServerState> type = new Type<ServerState>(ServerState::new, ServerState::fromNbt, DataFixTypes.LEVEL);

    public static ServerState getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        ServerState serverState = persistentStateManager.getOrCreate(type, "auction-house");
        return serverState;
    }

    public ArrayList<ItemStack> getAuctionList() {
        return auctionList;
    }

    public NbtList convertArrayListToNbtList(ArrayList<ItemStack> list) {
        NbtList nbtList = new NbtList();
        for (ItemStack stack : list) {
            NbtCompound compound = new NbtCompound();
            nbtList.add(stack.writeNbt(compound));
        }
        return nbtList;
    }

    public static ArrayList<ItemStack> convertNbtListToArrayList(NbtList nbtList) {
        ArrayList<ItemStack> arrayList = new ArrayList<ItemStack>();
        for (NbtElement element : nbtList) {
            ItemStack stack = ItemStack.fromNbt((NbtCompound)element);
            arrayList.add(stack);
        }
        return arrayList;
    }
    
}
