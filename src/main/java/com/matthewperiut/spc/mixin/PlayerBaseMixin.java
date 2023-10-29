package com.matthewperiut.spc.mixin;

import com.matthewperiut.spc.api.PlayerWarps;
import com.matthewperiut.spc.command.God;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.io.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Priority 999 to cancel fire even when in creative, because creative also cancels here.
// BHCreative uses priority 1000
@Mixin(value = PlayerBase.class, priority = 999)
public class PlayerBaseMixin implements PlayerWarps {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void onGod(EntityBase i, int par2, CallbackInfoReturnable<Boolean> cir) {
        PlayerBase player = (PlayerBase) (Object) this;

        if (God.isPlayerInvincible.containsKey(player.name))
            if (God.isPlayerInvincible.get(player.name)) {
                if (player.fire > 0)
                    player.fire = 0;

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

    @Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
    private void readFromTag(CompoundTag tag, CallbackInfo ci) {
        if (tag.containsKey("warps")) warpStr = tag.getString("warps");
    }

    @Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
    private void writeToTag(CompoundTag tag, CallbackInfo ci) {
        if (!warpStr.isEmpty()) tag.put("warps", warpStr);
    }
}
