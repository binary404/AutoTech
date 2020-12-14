package binary404.autotech.client.fx;

import binary404.autotech.AutoTech;
import binary404.autotech.client.fx.particles.EmpowerParticle;
import binary404.autotech.client.util.ModRenderTypes;
import binary404.autotech.common.core.util.Vector3;
import binary404.autotech.common.item.ModItems;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = AutoTech.modid, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class LaserRenderHandler {

    public static ArrayList<LaserInfo> lasers = new ArrayList<>();

    @OnlyIn(Dist.CLIENT)
    public static void addLaser(double x, double y, double z, double tx, double ty, double tz, int maxAge) {
        addLaser(x, y, z, tx, ty, tz, maxAge, false);
    }

    @OnlyIn(Dist.CLIENT)
    public static void addLaser(double x, double y, double z, double tx, double ty, double tz, int maxAge, boolean particles) {
        addLaser(x, y, z, tx, ty, tz, maxAge, 1.0F, 0.0F, 0.0F, 1.0F, 0.025F, particles);
    }

    @OnlyIn(Dist.CLIENT)
    public static void addLaser(double x, double y, double z, double tx, double ty, double tz, int maxAge, float r, float g, float b, float alpha, float thickness, boolean particles) {
        Vector3d source = new Vector3d(x, y, z);
        Vector3d target = new Vector3d(tx, ty, tz);
        LaserInfo info = new LaserInfo(maxAge, source, target, r, g, b, alpha, thickness, particles);
        lasers.add(info);
    }

    @SubscribeEvent
    public static void drawLasers(RenderWorldLastEvent event) {
        final Minecraft mc = Minecraft.getInstance();
        World world = mc.world;
        IRenderTypeBuffer.Impl buffer = mc.getRenderTypeBuffers().getBufferSource();
        long gameTime = world.getGameTime();
        double v = gameTime * 0.04;
        Vector3d view = mc.gameRenderer.getActiveRenderInfo().getProjectedView();
        MatrixStack stack = event.getMatrixStack();
        stack.push();
        stack.translate(-view.getX(), -view.getY(), -view.getZ());
        IVertexBuilder builder;
        builder = buffer.getBuffer(ModRenderTypes.LASER_MAIN_BEAM);

        for (int i = 0; i < lasers.size(); i++) {
            LaserInfo laser = lasers.get(i);
            if (laser != null) {
                stack.push();
                stack.translate(laser.source.getX(), laser.source.getY(), laser.source.getZ());
                float diffX = (float) (laser.target.getX() - laser.source.getX());
                float diffY = (float) (laser.target.getY() - laser.source.getY());
                float diffZ = (float) (laser.target.getZ() - laser.source.getZ());
                Vector3f startLaser = new Vector3f(0, 0, 0);
                Vector3f endLaser = new Vector3f(diffX, diffY, diffZ);
                Vector3f sortPos = new Vector3f((float) laser.source.getX(), (float) laser.source.getY(), (float) laser.source.getZ());

                Matrix4f matrix = stack.getLast().getMatrix();
                LaserRenderHelper.drawLaser(builder, matrix, endLaser, startLaser, laser.r, laser.g, laser.b, laser.alpha, laser.thickness, v, v + diffY * 1.5, sortPos);
                stack.pop();
            }
        }

        stack.pop();
        buffer.finish(ModRenderTypes.LASER_MAIN_BEAM);
    }

    @SubscribeEvent
    public static void updateLasers(TickEvent.ClientTickEvent event) {
        ArrayList<LaserInfo> toRemove = new ArrayList<>();

        for (int i = 0; i < lasers.size(); i++) {
            LaserInfo laser = lasers.get(i);
            if (laser != null) {
                laser.age++;
                if (laser.age > laser.maxAge) {
                    toRemove.add(laser);
                }
            }
        }

        for (LaserInfo laser : toRemove) {
            lasers.remove(laser);
        }
    }

    public static class LaserInfo {
        public int maxAge, age;
        public final float r, g, b, alpha, thickness;
        public final Vector3d source, target;
        public boolean particles;

        public LaserInfo(int maxAge, Vector3d source, Vector3d target, float r, float g, float b, float alpha, float thickness, boolean particles) {
            this.maxAge = maxAge;
            this.source = source;
            this.target = target;
            this.age = 0;
            this.r = r;
            this.g = g;
            this.b = b;
            this.alpha = alpha;
            this.thickness = thickness;
            this.particles = particles;
        }
    }

}
