package net.fabricmc.freecam.imixin;

public interface IGhost {
    void mITE_Neodymium$setPosition(float x, float y, float z);
    void mITE_Neodymium$setRotation(float rotationYaw, float rotationPitch);
    float mITE_Neodymium$getRotationYaw();
    void mITE_Neodymium$addPosition(int id, float xyz);
}
