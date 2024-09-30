package com.matthewperiut.spc.mixin.communicate;

import com.matthewperiut.spc.SPC;
import com.matthewperiut.spc.command.server.ServerUtil;
import net.minecraft.network.ClientPlayNetworkHandler;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.login.LoginRequest0x1Packet;
import net.minecraft.packet.play.UpdateSign0x82C2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Shadow public abstract void sendPacket(AbstractPacket arg);

    @Inject(method = "onLoginRequest", at = @At("TAIL"))
    void askIfOp(LoginRequest0x1Packet par1, CallbackInfo ci) {
        String[] contents = new String[]{"", "", "", ""};
        contents[0] = "op?";
        sendPacket(new UpdateSign0x82C2SPacket(0,-1,0, contents));
        SPC.mp_spc = false;
        SPC.mp_op = false;
    }

    @Inject(method = "onUpdateSign", at = @At("HEAD"))
    void customPacket(UpdateSign0x82C2SPacket par1, CallbackInfo ci) {
        if (par1.x == 0 && par1.y == -1 && par1.z == 0) {
            if (par1.length() > 0) {
                SPC.mp_spc = true;
                SPC.mp_op = par1.text[0].startsWith("1");
            }
        }
    }
}
