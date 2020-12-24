package us.zonix.anticheat.util;

import net.minecraft.server.v1_8_R3.*;

public class MathUtil
{
    public static float[] getRotationFromPosition(final CustomLocation playerLocation, final CustomLocation targetLocation) {
        final double xDiff = targetLocation.getX() - playerLocation.getX();
        final double zDiff = targetLocation.getZ() - playerLocation.getZ();
        final double yDiff = targetLocation.getY() - (playerLocation.getY() + 0.12);
        final double dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public static int pingFormula(final long ping) {
        return (int)Math.ceil(ping / 2L / 50.0) + 2;
    }
    
    public static float getDistanceBetweenAngles(final float angle1, final float angle2) {
        float distance = Math.abs(angle1 - angle2) % 360.0f;
        if (distance > 180.0f) {
            distance = 360.0f - distance;
        }
        return distance;
    }
}
