package net.fabricmc.freecam.network;

import btw.network.packet.handler.CustomPacketHandler;
import net.fabricmc.freecam.FreeCam;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet250CustomPayload;

import java.io.IOException;

public class FreeCamPacketHandler implements CustomPacketHandler {
    @Override
    public void handleCustomPacket(Packet250CustomPayload payload, EntityPlayer player) throws IOException {
        if (payload.data[0] == 1) {
            FreeCam.allowFreeCam = true;
        } else if (payload.data[0] == 0) {
            FreeCam.allowFreeCam = false;
        }
    }
}
