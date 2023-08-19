package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.api.PlayerWarps;
import com.matthewperiut.spc.api.PosParse;
import net.minecraft.entity.player.PlayerBase;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class Warp implements Command
{

    @Override
    public void command(PlayerBase player, String[] parameters)
    {

        if (parameters.length < 2)
        {
            manual();
            return;
        }

        if (parameters[1].equals("set"))
        {
            if (parameters[2].contains("|"))
            {
                sendMessage("Warp name cannot contain '|'");
                return;
            }

            PosParse pos = new PosParse(player);
            PlayerWarps pw = (PlayerWarps) player;
            String newStr = pw.spc$getWarpString() + parameters[2] + " " + pos.toString() + " ";

            pw.spc$setWarpString(newStr);
            sendMessage("Added " + parameters[2] + " to warps");
            return;
        }

        if (parameters[1].equals("tp"))
        {
            PlayerWarps pw = (PlayerWarps) player;
            String warps = pw.spc$getWarpString();
            String[] segments = warps.split(" ");

            for (int i = 0; i < segments.length; i += 4)
            {
                if (parameters[2].equals(segments[i]))
                {
                    PosParse pos = new PosParse(player, i + 1, segments);
                    if (pos.valid)
                    {
                        sendMessage("Teleported to " + parameters[2]);
                        Teleport.teleport(player, pos.x, pos.y + 0.1, pos.z);
                        return;
                    }
                }
            }

            sendMessage("Warp not found");
            return;
        }

        if (parameters[1].equals("list"))
        {
            PlayerWarps pw = (PlayerWarps) player;
            String warps = pw.spc$getWarpString();
            String[] segments = warps.split(" ");

            int pages = (int) Math.ceil((segments.length / 4.f) / 5.f);

            // 4 represents the number of segments per warp
            // 5 represents the number of warps per page

            int pg = 0;
            if (parameters.length > 2) pg = (Integer.parseInt(parameters[2]) - 1);

            int numSkipped = pg * 4 * 5;

            if (numSkipped > segments.length || numSkipped < 0)
            {
                sendMessage("Page not found");
                return;
            }

            int end = numSkipped + (4 * 5);
            if (end > segments.length) end = segments.length;

            sendMessage("Page " + (pg + 1) + "/" + pages);
            for (int i = numSkipped; i < end; i += 4)
            {
                System.out.println(numSkipped + " " + i + " " + (numSkipped + (4 * 5)));
                sendMessage(segments[i] + ": " + segments[i + 1] + " " + segments[i + 2] + " " + segments[i + 3]);
            }

            return;
        }

        manual();
    }

    @Override
    public String name()
    {
        return "warp";
    }

    @Override
    public void manual()
    {
        sendMessage("Usage 1: /warp {set/tp} {name}");
        sendMessage("Usage 2: /warp list {pg}");
        sendMessage("Info: Sets or teleports to a set point");
    }
}
