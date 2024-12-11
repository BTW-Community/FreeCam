package net.fabricmc.freecam.mixin;

import net.fabricmc.freecam.FreeCam;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.MovementInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityPlayerSP.class)
public class ClientPlayerMixin {

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/MovementInput;updatePlayerMoveState()V"))
    public void updatePlayerMoveState(MovementInput movementInput) {
        if (FreeCam.isFreeCam) {
            FreeCam.movementInput.updatePlayerMoveState();
        } else {
            movementInput.updatePlayerMoveState();
        }
    }
}
