package com.periut.retrocommands.mixin.access;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("dataTracker")
    net.minecraft.entity.data.DataTracker getDataTracker();
}
