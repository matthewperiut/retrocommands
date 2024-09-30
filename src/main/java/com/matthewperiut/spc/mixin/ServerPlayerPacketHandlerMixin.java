package com.matthewperiut.spc.mixin;

import com.matthewperiut.spc.command.server.ServerUtil;
import com.matthewperiut.spc.util.SPChatUtil;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.play.ChatMessage0x3Packet;
import net.minecraft.server.network.ServerPlayerPacketHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerPacketHandler.class)
public abstract class ServerPlayerPacketHandlerMixin {
    @Shadow private ServerPlayer serverPlayer;

    @Shadow public abstract void send(AbstractPacket arg);

    @Inject(method = "parseCommand(Ljava/lang/String;)V", at = @At(value = "HEAD"), cancellable = true)
    private void parseRegularCommand(String string, CallbackInfo ci) {
        if (!ServerUtil.getConnectionManager().isOp(serverPlayer.name))
            if (SPChatUtil.handleCommand(new SharedCommandSource(this), string.substring(1), false))
                ci.cancel();
    }

    /*
    @Inject(method = "sendFeedback", at = @At(value = "HEAD"), cancellable = true)
    private void sendFeedback(String message, CallbackInfo ci) {
        send(new ChatMessage0x3Packet(message));
        ci.cancel();
    }*/
}
