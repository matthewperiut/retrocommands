package com.matthewperiut.spc.mixin.communicate;

import com.matthewperiut.spc.command.server.ServerUtil;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.play.UpdateSign0x82C2SPacket;
import net.minecraft.server.network.ServerPlayerPacketHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerPacketHandler.class)
public abstract class ServerPlayPacketHandlerMixin {
    @Shadow public abstract void send(AbstractPacket arg);

    @Shadow private ServerPlayer serverPlayer;

    @Inject(method = "onUpdateSign", at = @At("HEAD"), cancellable = true)
    void customPacket(UpdateSign0x82C2SPacket par1, CallbackInfo ci) {
        if (par1.x == 0 && par1.y == -1 && par1.z == 0) {
            if (par1.text[0].equals("op?")) {
                String[] contents = new String[]{"", "", "", ""};
                contents[0] = ServerUtil.isOp(serverPlayer.name) ? "1" : "0";
                send(new UpdateSign0x82C2SPacket(0, -1, 0, contents));
                ci.cancel();
            }
        }
    }
}
