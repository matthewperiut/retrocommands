package com.matthewperiut.retrocommands.mixin;

import com.matthewperiut.retrocommands.api.ItemInstanceStr;
import com.matthewperiut.retrocommands.optionaldep.stapi.block.SpecialMobSpawnerBlock;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Block.class)
public class BlockBaseMixin {
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(II)Lnet/minecraft/block/SpawnerBlock;"))
    private static SpawnerBlock staticBlock(int i, int j) {
        if (FabricLoader.getInstance().isModLoaded("station-blockitems-v0"))
            return new SpecialMobSpawnerBlock(i, j);
        else
            return new SpawnerBlock(i, j);
    }

    @Inject(method = "onPlaced(Lnet/minecraft/world/World;IIII)V", at = @At("HEAD"))
    public void onBlockPlaced(World i, int j, int k, int l, int direction, CallbackInfo ci) {
        if (Block.BLOCKS[i.getBlockId(j, k, l)].getTranslatedName().startsWith("Mon")) //ster Spawner
        {
            try {
                Box b = Box.create(j - 5, k - 5, l - 5, j + 5, k + 5, l + 5);
                List<PlayerEntity> players = i.collectEntitiesByClass(PlayerEntity.class, b);
                String mob = null;
                if (players.size() == 1) {
                    if (players.get(0).inventory.getSelectedItem() != null)
                        mob = ((ItemInstanceStr) (Object) players.get(0).inventory.getSelectedItem()).spc$getStr();
                } else {
                    for (PlayerEntity p : players) {
                        if (p.getHand().getItem().getTranslatedName().startsWith("Mon")) {
                            if (p.inventory.getSelectedItem() != null)
                                mob = ((ItemInstanceStr) (Object) p.inventory.getSelectedItem()).spc$getStr();
                            break;
                        }
                    }
                }
                if (mob == null)
                    mob = "Pig";

                Entity entity = EntityRegistry.create(mob, i);
                if (entity instanceof LivingEntity) {
                    ((MobSpawnerBlockEntity) i.getBlockEntity(j, k, l)).setSpawnedEntityId(entity.getRegistryEntry());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
