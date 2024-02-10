package in.techpro424.auctionhouse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import in.techpro424.auctionhouse.AuctionHouse;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void onPlayerConnect(ClientConnection clientConnection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        AuctionHouse.sidebar.addPlayer(player);
        AuctionHouse.refreshSidebar();
    }
    
}
