package com.matthewperiut.retrocommands.mixin.communicate;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.play.UpdateSignPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.DataInputStream;
import java.util.Arrays;

@Mixin(UpdateSignPacket.class)
abstract public class UpdateSignPacketMixin extends Packet {
    @Shadow public int x;

    @Shadow public int y;

    @Shadow public int z;

    @Shadow public String[] text;

    /**
     * @author matthewperiut
     * @reason Sign shenanigans
     */
    @Overwrite
    public void read(DataInputStream stream) {
        try {
            this.x = stream.readInt();
            this.y = stream.readShort();
            this.z = stream.readInt();

            int max = 15;
            if (this.x == 0 && this.y == -1 && this.z == 0) {
                max = 2000;
            }

            this.text = new String[4];

            for(int var2 = 0; var2 < 4; ++var2) {
                this.text[var2] = readString(stream, max);
            }
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
