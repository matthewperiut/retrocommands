package com.matthewperiut.retrocommands.mixin.feature;

import com.matthewperiut.retrocommands.api.ItemInstanceStr;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
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

    @Inject(method = "writeNbt", at = @At("RETURN"))
    private void onToTag(NbtCompound arg, CallbackInfoReturnable<NbtCompound> cir) {
        if (this.name != null)
            arg.put("spcName", new NbtString(this.name));
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    private void onFromTag(NbtCompound arg, CallbackInfo ci) {
        if (arg.contains("spcName")) {
            this.name = arg.getString("spcName");
        }
    }


    @Inject(method = "split", at = @At("RETURN"))
    private void onSplit(int i, CallbackInfoReturnable<ItemStack> cir) {
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
