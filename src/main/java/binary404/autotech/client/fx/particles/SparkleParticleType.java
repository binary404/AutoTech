package binary404.autotech.client.fx.particles;

import binary404.fx_lib.fx.FXGeneric;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

import javax.annotation.Nullable;
import java.util.Random;

public class SparkleParticleType extends BasicParticleType {

    public SparkleParticleType() {
        super(false);
    }

    public static class Factory implements IParticleFactory<BasicParticleType> {

        public Factory(IAnimatedSprite sprite) {

        }

        @Nullable
        @Override
        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Random rand = worldIn.rand;

            double d0 = (double) rand.nextFloat() * -1.9D * (double) rand.nextFloat() * 0.1D;
            double d1 = (double) rand.nextFloat() * -0.5D * (double) rand.nextFloat() * 0.1D * 5.0D;
            double d2 = (double) rand.nextFloat() * -1.9D * (double) rand.nextFloat() * 0.1D;

            FXGeneric fx = new FXGeneric(worldIn, x, y, z, d0, d1, d2, FXHelper.particleLocation);
            fx.setColor(0.1F, 0.7F, 0.2F);
            fx.setScale(0.15F, 0.05F, 0.0F);
            fx.setGridSize(64);
            fx.setMaxAge(100);
            fx.setParticles(320, 16, 1);
            fx.loop = true;
            fx.setAlphaFA(1.0F);
            fx.setGravity(0.01F);
            return fx;
        }
    }
}
