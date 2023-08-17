package com.matthewperiut.spc.optionaldep.stapi.item;

import com.matthewperiut.spc.api.ItemInstanceStr;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.gui.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateBlock;

public class MobSpawnerBlockItem extends TemplateBlock implements CustomTooltipProvider {
    public MobSpawnerBlockItem(int i) {
        super(i);
    }

    @Override
    public String[] getTooltip(ItemInstance itemInstance, String originalTooltip) {

        if (((Object) itemInstance) instanceof ItemInstanceStr item) {
            String[] og = originalTooltip.split(" ");
            if (item.spc$getStr() != null) {
                og[0] = item.spc$getStr();
                if (og[0].toLowerCase().startsWith("entity")) {
                    og[0] = og[0].substring(6);
                }
                if (og[0].startsWith("_")) {
                    og[0] = og[0].substring(1);
                }
                String construct = "";
                for (int i = 0; i < og[0].length(); i++) {
                    char c = og[0].charAt(i);
                    if (Character.isUpperCase(c)) {
                        if (i > 0)
                            construct += " ";
                    }
                    construct += c;
                }
                og[0] = construct;

                og[0] = og[0].substring(0, 1).toUpperCase() + og[0].substring(1);
            } else
                og[0] = "Pig";
            String newTooltip = og[0] + " " + og[1];
            return new String[]{newTooltip};
        } else {
            return new String[]{originalTooltip};
        }
    }
}
