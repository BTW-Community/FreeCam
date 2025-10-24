package net.fabricmc.freecam.mixin;

import net.fabricmc.freecam.FreeCam;
import net.fabricmc.freecam.imixin.IGhost;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin implements IGhost {
    @Unique
    private float motionX;
    @Unique
    private float motionY;
    @Unique
    private float motionZ;
    @Unique
    private float prevMotionX;
    @Unique
    private float prevMotionY;
    @Unique
    private float prevMotionZ;
    @Unique
    private float rotationYaw = 0;
    @Unique
    private float rotationPitch = 0;
    @Unique
    private float prevRotationYaw;
    @Unique
    private float prevRotationPitch;

    @Inject(method = "updateCameraAndRender", at = @At("HEAD"))
    private void injectZoomCamera(float par1, CallbackInfo ci) {

    }

    @Redirect(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityClientPlayerMP;setAngles(FF)V"))
    public void setAngles(EntityClientPlayerMP entityClientPlayerMP, float par1, float par2) {
        if (FreeCam.isFreeCam) {
            float var3 = this.rotationPitch;
            float var4 = this.rotationYaw;
            this.rotationYaw = (float) ((double) this.rotationYaw + (double) par1 * 0.15);
            this.rotationPitch = (float) ((double) this.rotationPitch - (double) par2 * 0.15);
            if (this.rotationPitch < -90.0f) {
                this.rotationPitch = -90.0f;
            }
            if (this.rotationPitch > 90.0f) {
                this.rotationPitch = 90.0f;
            }
            this.prevRotationPitch += this.rotationPitch - var3;
            this.prevRotationYaw += this.rotationYaw - var4;
        } else {
            entityClientPlayerMP.setAngles(par1, par2);
        }
    }

    @Inject(method = "orientCamera", at = @At("RETURN"))
    public void setCameraPos(float par1, CallbackInfo ci) {
        if (!FreeCam.isFreeCam) return;

//        if (FreeCam.movementInput.sneak) {
//            this.motionY += 0.5F;
//        }
//        if (FreeCam.movementInput.jump) {
//            this.motionY -= 0.5F;
//        }
//
//        float moveForward = FreeCam.movementInput.moveForward;
//        float moveStrafe = FreeCam.movementInput.moveStrafe;
//        float var4 = moveForward * moveForward + moveStrafe * moveStrafe;
//        if (var4 >= 1.0E-4f) {
//            if ((var4 = MathHelper.sqrt_float(var4)) < 1.0f) {
//                var4 = 1.0f;
//            }
//            var4 = 0.5f / var4;
//            float var5 = MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0f);
//            float var6 = MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0f);
//            this.motionX += (moveForward *= var4) * -var6 + (moveStrafe *= var4) * var5;
//            this.motionZ += moveStrafe * var6 + moveForward * var5;
//        }

        float var5 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * par1;
        float var6 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * par1;

        GL11.glRotatef(var5, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(var6, MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0f),
                0, MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0f));

        this.prevMotionX = this.prevMotionX + (this.motionX - this.prevMotionX) * par1;
        this.prevMotionY = this.prevMotionY + (this.motionY - this.prevMotionY) * par1;
        this.prevMotionZ = this.prevMotionZ + (this.motionZ - this.prevMotionZ) * par1;

        GL11.glTranslatef(prevMotionX , prevMotionY, prevMotionZ);

        this.prevRotationYaw = rotationYaw;
        this.prevRotationPitch = rotationPitch;
    }

    @Redirect(method = "orientCamera", at = @At(value = "INVOKE",
            target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V",
            ordinal = 11, remap = false))
    public void glRotatef(float angle, float x, float y, float z) {
        if (!FreeCam.isFreeCam) {
            GL11.glRotatef(angle, x, y, z);
        }
    }

    @Redirect(method = "orientCamera", at = @At(value = "INVOKE",
            target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V",
            ordinal = 12, remap = false))
    public void glRotatef1(float angle, float x, float y, float z) {
        if (!FreeCam.isFreeCam) {
            GL11.glRotatef(angle, x, y, z);
        }
    }

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    public void renderHand(float par1, int par2, CallbackInfo ci) {
        if (FreeCam.isFreeCam) {
            ci.cancel();
        }
    }

    @Override
    public void mITE_Neodymium$setPosition(float x, float y, float z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        this.prevMotionX = x;
        this.prevMotionY = y;
        this.prevMotionZ = z;
    }

    @Override
    public void mITE_Neodymium$setRotation(float rotationYaw, float rotationPitch) {
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
    }

    @Override
    public float mITE_Neodymium$getRotationYaw() {
        return this.rotationYaw;
    }

    @Override
    public void mITE_Neodymium$addPosition(int id, float xyz) {
        if (id == 0) {
            this.motionX += xyz;
        } else if (id == 1) {
            this.motionY += xyz;
        } else if (id == 2) {
            this.motionZ += xyz;
        }
    }
}
