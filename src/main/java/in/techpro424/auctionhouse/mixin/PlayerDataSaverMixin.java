package in.techpro424.auctionhouse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import in.techpro424.auctionhouse.util.PlayerEntityDataSaverInterface;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerDataSaverMixin implements PlayerEntityDataSaverInterface {
    private NbtCompound persistentData;

    @Override
    public NbtCompound getPersistentData() {
        if(this.persistentData == null) {
            this.persistentData = new NbtCompound();
        }

        return persistentData;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    protected void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        if(persistentData != null) nbt.put("auction-house-balance", persistentData);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    protected void readNbt(NbtCompound nbt, CallbackInfo ci) {
        if(nbt.contains("auction-house-balance", 10)) {
            persistentData = nbt.getCompound("auction-house-balance");
        }
    }
}
