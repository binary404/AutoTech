package binary404.autotech.client.fx.particles;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.BreakingParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class EmpowerParticle extends BreakingParticle {

    private BlockState block;
    private ItemStack item;
    private double targetX;
    private double targetY;
    private double targetZ;

    public EmpowerParticle(ClientWorld world, double par1, double par2, double par3, double tx, double ty, double tz, BlockState block) {
        super(world, par1, par2, par3, ItemStack.EMPTY);
        this.block = block;
        setSprite(Minecraft.getInstance().getItemRenderer().getItemModelMesher().getParticleIcon(Item.getItemFromBlock(block.getBlock())));

        this.targetX = tx;
        this.targetY = ty;
        this.targetZ = tz;
        double dx = tx - this.posX;
        double dy = ty - this.posY;
        double dz = tz - this.posZ;
        int base = (int) (MathHelper.sqrt(dx * dx + dy * dy + dz * dz) * 10.0F);
        if (base < 1)
            base = 1;
        this.maxAge = base / 2 + this.rand.nextInt(base);

        float f3 = 0.01F;
        this.motionX = ((float) this.rand.nextGaussian() * f3);
        this.motionY = ((float) this.rand.nextGaussian() * f3);
        this.motionZ = ((float) this.rand.nextGaussian() * f3);

        this.particleGravity = 0.01F;
    }

    public EmpowerParticle(ClientWorld world, double par1, double par2, double par3, double tx, double ty, double tz, ItemStack item) {
        super(world, par1, par2, par3, ItemStack.EMPTY);
        this.item = item;
        setSprite(Minecraft.getInstance().getItemRenderer().getItemModelMesher().getParticleIcon(item.getItem()));

        this.targetX = tx;
        this.targetY = ty;
        this.targetZ = tz;
        double dx = tx - this.posX;
        double dy = ty - this.posY;
        double dz = tz - this.posZ;
        int base = (int) (MathHelper.sqrt(dx * dx + dy * dy + dz * dz) * 10.0F);
        if (base < 1)
            base = 1;
        this.maxAge = base / 2 + this.rand.nextInt(base);

        float f3 = 0.01F;
        this.motionX = ((float) this.rand.nextGaussian() * f3);
        this.motionY = ((float) this.rand.nextGaussian() * f3);
        this.motionZ = ((float) this.rand.nextGaussian() * f3);

        this.particleGravity = 0.01F;
    }

    @Override
    public void tick() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (age++ >= maxAge ||
                (MathHelper.floor(posX) == MathHelper.floor(targetX) &&
                        MathHelper.floor(posY) == MathHelper.floor(targetY) &&
                        MathHelper.floor(posZ) == MathHelper.floor(targetZ))) {
            setExpired();
            return;
        }

        move(this.motionX, this.motionY, this.motionZ);

        this.motionX *= 0.985D;
        this.motionY *= 0.95D;
        this.motionZ *= 0.985D;

        double dx = targetX - posX;
        double dy = targetY - posY;
        double dz = targetZ - posZ;
        double d11 = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
        double clamp = Math.min(0.25D, d11 / 15.0D);

        if (d11 < 2.0D) {
            this.particleScale *= 0.9F;
        }
        dx /= d11;
        dy /= d11;
        dz /= d11;

        this.motionX += dx * clamp;
        this.motionY += dy * clamp;
        this.motionZ += dz * clamp;

        this.motionX = MathHelper.clamp((float) this.motionX, -clamp, clamp);
        this.motionY = MathHelper.clamp((float) this.motionY, -clamp, clamp);
        this.motionZ = MathHelper.clamp((float) this.motionZ, -clamp, clamp);

        this.motionX += this.rand.nextGaussian() * 0.007D;
        this.motionY += this.rand.nextGaussian() * 0.007D;
        this.motionZ += this.rand.nextGaussian() * 0.007D;
    }
}
