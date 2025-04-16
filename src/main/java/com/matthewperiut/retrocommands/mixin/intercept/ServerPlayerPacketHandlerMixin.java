package com.matthewperiut.retrocommands.mixin.intercept;

import com.matthewperiut.retrocommands.util.ServerUtil;
import com.matthewperiut.retrocommands.util.RetroChatUtil;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayerPacketHandlerMixin {
    @Shadow private ServerPlayerEntity player;

    @Shadow public abstract void sendPacket(Packet arg);

    @Inject(method = "handleCommand", at = @At(value = "HEAD"), cancellable = true)
    private void parseRegularCommand(String string, CallbackInfo ci) {
        if (!ServerUtil.getConnectionManager().isOperator(player.name))
            if (RetroChatUtil.handleCommand(new SharedCommandSource(this), string.substring(1), false))
                ci.cancel();
    }
}
