package com.matthewperiut.spc.mixin;

import com.matthewperiut.spc.command.server.ServerUtil;
import com.matthewperiut.spc.util.SPChatUtil;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public class CommandManagerMixin {
    @Inject(method = "processCommand", at = @At(value = "HEAD"), cancellable = true)
    private void processSPCommands(Command par1, CallbackInfo ci) {
        String command = par1.commandString;

        SPChatUtil.handleCommand(new SharedCommandSource(par1.source), command, true);

        ci.cancel();
    }
}
