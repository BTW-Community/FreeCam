package net.fabricmc.freecam.mixin;

import net.fabricmc.freecam.FreeCam;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    public GameSettings gameSettings;

    @Inject(method = "loadWorld(Lnet/minecraft/src/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
    private void loadWorldMixin(WorldClient par1WorldClient, String par2Str, CallbackInfo ci) {
        FreeCam.movementInput = new MovementInputFromOptions(this.gameSettings);
    }

    @Inject(method = "setDimensionAndSpawnPlayer", at = @At("HEAD"))
    public void setDimensionAndSpawnPlayer(int par1, CallbackInfo ci) {
        FreeCam.movementInput = new MovementInputFromOptions(this.gameSettings);
    }

    @Inject(method = "startGame", at= @At("RETURN"))
    private void startGameMixin(CallbackInfo ci) {
        FreeCam.mouseHelper = new MouseHelper();
    }

    @Inject(method = "setIngameFocus", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/MouseHelper;grabMouseCursor()V"))
    public void grabMouseCursor(CallbackInfo ci) {
        if (FreeCam.isFreeCam) {
            FreeCam.mouseHelper.grabMouseCursor();
        }
    }

    @Inject(method = "setIngameNotInFocus", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/MouseHelper;ungrabMouseCursor()V"))
    public void ungrabMouseCursor(CallbackInfo ci) {
        if (FreeCam.isFreeCam) {
            FreeCam.mouseHelper.ungrabMouseCursor();
        }
    }
}
