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
        this(player.x, player.y, player.z, start, segments);
    }

    public PosParse(double x, double y, double z, int start, String[] segments) {
        try {
            String sx = segments[start];
            String sy = segments[start + 1];
            String sz = segments[start + 2];

            if (sx.charAt(0) == '~') {
                sx = sx.substring(1);
                this.x += x;
            }
            if (sy.charAt(0) == '~') {
                sy = sy.substring(1);
                this.y += y;
            }
            if (sz.charAt(0) == '~') {
                sz = sz.substring(1);
                this.z += z;
            }

            if (!sx.isEmpty())
                this.x += Double.parseDouble(sx);
            if (!sy.isEmpty())
                this.y += Double.parseDouble(sy);
            if (!sz.isEmpty())
                this.z += Double.parseDouble(sz);
        } catch (NumberFormatException e) {
            valid = false;
        }
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f %.1f", x, y, z);
    }
}
