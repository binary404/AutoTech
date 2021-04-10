package binary404.autotech.client.fx.particles;
import binary404.fx_lib.fx.particles.ParticleDispatcher;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public class FXHelper {

    public static ResourceLocation particleLocation = new ResourceLocation("autotech", "textures/misc/particles.png");

    public static Random rand = new Random();

    public static ParticleDispatcher.GenPart getGenPart(float r, float g, float b, float scale) {
        ParticleDispatcher.GenPart part = new ParticleDispatcher.GenPart();
        part.r = r;
        part.g = g;
        part.b = b;
        part.scale = new float[]{scale};
        part.location = particleLocation;
        return part;
    }

    public static void sparkle(double x, double y, double z, double mx, double my, double mz, float r, float g, float b, float scale, float decay, float grav, int baseAge) {
        ParticleDispatcher.GenPart part = getGenPart(r, g, b, scale);
        int age = baseAge * 4 + rand.nextInt(baseAge);
        boolean sp = (rand.nextFloat() < 0.2D);
        part.scale = new float[]{scale * 2F, scale / 3};
        part.grid = 64;
        part.age = age;
        part.partStart = sp ? 320 : 512;
        part.partNum = 16;
        part.partInc = 1;
        part.loop = true;
        part.alpha = new float[]{1.0F};
        part.slowDown = decay;
        part.grav = grav;
        ParticleDispatcher.genericFx(x, y, z, mx, my, mz, part);
    }

}
