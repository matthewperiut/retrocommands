package com.matthewperiut.spc.mixin;

import net.minecraft.entity.EntityBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityBase.class)
public interface EntityAccessor {
    @Accessor("dataTracker")
    net.minecraft.entity.data.DataTracker getDataTracker();
}
