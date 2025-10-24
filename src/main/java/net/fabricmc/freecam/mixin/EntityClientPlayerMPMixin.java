package net.fabricmc.freecam.mixin;

import net.fabricmc.freecam.FreeCam;
import net.fabricmc.freecam.imixin.IGhost;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Minecraft;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityClientPlayerMP.class)
public class EntityClientPlayerMPMixin {
    @Inject(method = "onUpdate", at = @At("HEAD"))
    private void updateCamMoveState(CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.thePlayer == null || !FreeCam.isFreeCam) return;

        IGhost renderer = (IGhost) Minecraft.getMinecraft().entityRenderer;

        if (FreeCam.movementInput.sneak) {
            renderer.mITE_Neodymium$addPosition(1, FreeCam.speed);
        }
        if (FreeCam.movementInput.jump) {
            renderer.mITE_Neodymium$addPosition(1, -FreeCam.speed);
        }

        float moveForward = FreeCam.movementInput.moveForward;
        float moveStrafe = FreeCam.movementInput.moveStrafe;
        float var4 = moveForward * moveForward + moveStrafe * moveStrafe;
        if (var4 >= 1.0E-4f) {
            if ((var4 = MathHelper.sqrt_float(var4)) < 1.0f) {
                var4 = 1.0f;
            }
            var4 = FreeCam.speed / var4;
            float var5 = MathHelper.cos(renderer.mITE_Neodymium$getRotationYaw() * (float)Math.PI / 180.0f);
            float var6 = MathHelper.sin(renderer.mITE_Neodymium$getRotationYaw() * (float)Math.PI / 180.0f);
            renderer.mITE_Neodymium$addPosition(0, (moveForward *= var4) * -var6 + (moveStrafe *= var4) * var5);
            renderer.mITE_Neodymium$addPosition(2, moveStrafe * var6 + moveForward * var5);
        }

        int wheel = Mouse.getDWheel();
        if (wheel != 0 && mc.currentScreen == null) {

            float speedChange = wheel > 0 ? 0.05f : -0.05f;

            FreeCam.speed = MathHelper.clamp_float(FreeCam.speed + speedChange, 0.05f, 2.0f);
        }
    }
}
