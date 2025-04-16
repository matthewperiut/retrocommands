package com.matthewperiut.retrocommands.mixin.feature;

import com.matthewperiut.retrocommands.api.PlayerWarps;
import com.matthewperiut.retrocommands.command.extra.God;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Priority 999 to cancel fire even when in creative, because creative also cancels here.
// BHCreative uses priority 1000
@Mixin(value = PlayerEntity.class, priority = 999)
public class PlayerBaseMixin implements PlayerWarps {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void onGod(Entity i, int par2, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (God.isPlayerInvincible.containsKey(player.name))
            if (God.isPlayerInvincible.get(player.name)) {
                if (player.fireTicks > 0)
                    player.fireTicks = 0;

                cir.cancel();
            }
    }

    @Unique
    String warpStr = "";

    @Override
    public void spc$setWarpString(String warp) {
        warpStr = warp;
    }

    @Override
    public String spc$getWarpString() {
        return warpStr;
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void readFromTag(NbtCompound tag, CallbackInfo ci) {
        if (tag.contains("warps")) warpStr = tag.getString("warps");
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void writeToTag(NbtCompound tag, CallbackInfo ci) {
        if (!warpStr.isEmpty()) tag.putString("warps", warpStr);
    }
}
