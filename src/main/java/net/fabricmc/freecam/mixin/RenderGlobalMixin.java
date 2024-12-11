package net.fabricmc.freecam.mixin;

import net.fabricmc.freecam.FreeCam;
import net.minecraft.src.GameSettings;
import net.minecraft.src.RenderGlobal;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderGlobal.class)
public abstract class RenderGlobalMixin {
    @Redirect(method = "renderEntities", at = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;thirdPersonView:I",
            opcode = Opcodes.GETFIELD))
    public int getThirdPersonView(GameSettings gameSettings) {
        if (FreeCam.isFreeCam) {
            return 2;
        }
        return gameSettings.thirdPersonView;
    }
}
