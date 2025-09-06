package net.fabricmc.freecam.mixin;

import net.fabricmc.freecam.FreeCam;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Minecraft;
import net.minecraft.src.NetClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetClientHandler.class)
public class NetClientHandlerMixin {
    @Inject(method = "<init>(Lnet/minecraft/src/Minecraft;Ljava/lang/String;ILnet/minecraft/src/GuiScreen;)V", at = @At("RETURN"))
    private void injectInit(Minecraft par1Minecraft, String par2Str, int par3, GuiScreen par4GuiScreen, CallbackInfo ci) {
        FreeCam.allowFreeCam = false;
    }

    @Inject(method = "<init>(Lnet/minecraft/src/Minecraft;Ljava/lang/String;I)V", at = @At("RETURN"))
    private void injectInit(Minecraft par1Minecraft, String par2Str, int par3, CallbackInfo ci) {
        FreeCam.allowFreeCam = false;
    }
}
