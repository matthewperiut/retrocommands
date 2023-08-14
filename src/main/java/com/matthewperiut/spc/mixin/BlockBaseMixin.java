package com.matthewperiut.spc.mixin;

import com.matthewperiut.spc.api.ItemInstanceStr;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.maths.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BlockBase.class)
public class BlockBaseMixin {
    @Inject(method = "onBlockPlaced(Lnet/minecraft/level/Level;IIII)V", at = @At("HEAD"))
    public void onBlockPlaced(Level i, int j, int k, int l, int par5, CallbackInfo ci) {
        if (BlockBase.BY_ID[i.getTileId(j, k, l)].getTranslatedName().startsWith("Mon")) //ster Spawner
        {
            try {
                Box b = Box.create(j - 5, k - 5, l - 5, j + 5, k + 5, l + 5);
                List<PlayerBase> players = i.getEntities(PlayerBase.class, b);
                String mob = null;
                if (players.size() == 1) {
                    if (players.get(0).inventory.getHeldItem() != null)
                        mob = ((ItemInstanceStr) (Object) players.get(0).inventory.getHeldItem()).spc$getStr();
                } else {
                    for (PlayerBase p : players) {
                        if (p.getHeldItem().getType().getTranslatedName().startsWith("Mon")) {
                            if (p.inventory.getHeldItem() != null)
                                mob = ((ItemInstanceStr) (Object) p.inventory.getHeldItem()).spc$getStr();
                            break;
                        }
                    }
                }
                if (mob == null)
                    mob = "Pig";

                EntityBase entity = EntityRegistry.create(mob, i);
                if (entity instanceof Living) {
                    ((TileEntityMobSpawner) i.getTileEntity(j, k, l)).setEntityId(entity.getStringId());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }
}
