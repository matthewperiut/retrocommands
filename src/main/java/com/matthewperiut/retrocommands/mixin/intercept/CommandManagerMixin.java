package com.matthewperiut.retrocommands.mixin.intercept;

import com.matthewperiut.retrocommands.util.RetroChatUtil;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.ServerCommandHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommandHandler.class)
public class CommandManagerMixin {
    @Inject(method = "executeCommand", at = @At(value = "HEAD"), cancellable = true)
    private void processSPCommands(Command par1, CallbackInfo ci) {
        String command = par1.commandAndArgs;

        RetroChatUtil.handleCommand(new SharedCommandSource(par1.output), command, true);

        ci.cancel();
    }
}
