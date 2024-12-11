package btw.community.freecam;

import btw.AddonHandler;
import btw.BTWAddon;
import net.fabricmc.freecam.network.FreeCamPacketHandler;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet250CustomPayload;

import java.util.Map;

public class FreeCamAddon extends BTWAddon {
    private static FreeCamAddon instance;
    public static final String CUSTOM_FREE_CAM_PACKET = "freecam|AW";
    public static boolean allowFreeCam;
    public FreeCamAddon() {
        super();
        instance = this;
    }

    @Override
    public void preInitialize() {
        registerConfigProperties();
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
        instance.registerPacketHandler(CUSTOM_FREE_CAM_PACKET, new FreeCamPacketHandler());
    }

    @Override
    public void serverPlayerConnectionInitialized(NetServerHandler serverHandler, EntityPlayerMP playerMP) {
        serverHandler.sendPacket(new Packet250CustomPayload(CUSTOM_FREE_CAM_PACKET, new byte[]{(byte) (allowFreeCam? 1: 0)}));
    }

    private void registerConfigProperties() {
        this.registerProperty("allowFreeCam", "True", "Allow clients to toggle free cameras");
    }

    @Override
    public void handleConfigProperties(Map<String, String> propertyValues) {
        allowFreeCam = Boolean.parseBoolean(propertyValues.get("allowFreeCam"));
    }
}