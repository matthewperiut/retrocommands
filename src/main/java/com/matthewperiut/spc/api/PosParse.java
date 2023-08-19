package com.matthewperiut.spc.api;

import net.minecraft.entity.player.PlayerBase;

public class PosParse {
    public double x;
    public double y;
    public double z;

    public boolean valid = true;

    public PosParse(PlayerBase player)
    {
        x = player.x;
        y = player.y;
        z = player.z;
    }

    public PosParse(PlayerBase player, int start, String[] segments) {
        try {
            String sx = segments[start];
            String sy = segments[start + 1];
            String sz = segments[start + 2];

            if (sx.charAt(0) == '~') {
                sx = sx.substring(1);
                x += player.x;
            }
            if (sy.charAt(0) == '~') {
                sy = sy.substring(1);
                y += player.y;
            }
            if (sz.charAt(0) == '~') {
                sz = sz.substring(1);
                z += player.z;
            }

            if (!sx.isEmpty())
                x += Double.parseDouble(sx);
            if (!sy.isEmpty())
                y += Double.parseDouble(sy);
            if (!sz.isEmpty())
                z += Double.parseDouble(sz);
        } catch (NumberFormatException e) {
            valid = false;
        }
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f %.1f", x, y, z);
    }
}
