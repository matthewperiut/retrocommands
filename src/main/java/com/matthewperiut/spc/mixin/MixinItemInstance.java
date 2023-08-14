package com.matthewperiut.spc.mixin;

import com.matthewperiut.spc.api.ItemInstanceStr;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.StringTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInstance.class)
public abstract class MixinItemInstance implements ItemInstanceStr {
    @Unique
    private String name;

    @Override
    public String spc$getStr() {
        return this.name;
    }

    @Override
    public void spc$setStr(String name) {
        this.name = name;
    }

    @Inject(method = "toTag", at = @At("RETURN"))
    private void onToTag(CompoundTag arg, CallbackInfoReturnable<CompoundTag> cir) {
        if (this.name != null)
            arg.put("spcName", new StringTag(this.name));
    }

    @Inject(method = "fromTag", at = @At("HEAD"))
    private void onFromTag(CompoundTag arg, CallbackInfo ci) {
        if (arg.containsKey("spcName")) {
            this.name = arg.getString("spcName");
        }
    }


    @Inject(method = "split", at = @At("RETURN"))
    private void onSplit(int i, CallbackInfoReturnable<ItemInstance> cir) {
        ItemInstanceStr copyMixin = (ItemInstanceStr) (Object) cir.getReturnValue();
        copyMixin.spc$setStr(this.name);
    }


    /*
    @Inject(method = "copy()Lnet/minecraft/item/ItemInstance;", at = @At("RETURN"))
    private void onCopy(CallbackInfoReturnable<ItemInstance> cir) {
        ItemInstanceStr copyMixin = (ItemInstanceStr) (Object) cir.getReturnValue();
        if (this.name != null && cir.getReturnValue() != null)
            copyMixin.spc$setStr(this.name);
        else
            System.out.println("didn't work");
    }
*/
}
