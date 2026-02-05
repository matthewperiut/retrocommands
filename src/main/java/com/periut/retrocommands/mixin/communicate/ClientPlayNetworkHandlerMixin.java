package com.periut.retrocommands.mixin.communicate;
 
 import com.periut.retrocommands.RetroCommands;
 import net.minecraft.client.network.ClientNetworkHandler;
 import net.minecraft.network.packet.login.LoginHelloPacket;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Inject;
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientNetworkHandler.class)
 public abstract class ClientPlayNetworkHandlerMixin {
     @Inject(method = "onHello", at = @At("TAIL"))
     void askIfOp(LoginHelloPacket par1, CallbackInfo ci) {
         RetroCommands.mp_rc = false;
         RetroCommands.mp_op = false;
     }
 }