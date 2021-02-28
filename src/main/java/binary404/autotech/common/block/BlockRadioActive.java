package binary404.autotech.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockRadioActive extends Block {

    public BlockRadioActive() {
        super(Properties.create(Material.CLAY).sound(SoundType.STONE).slipperiness(0.8F).notSolid().setLightLevel((state) -> 15));
    }

    @Override
    public MaterialColor getMaterialColor() {
        return MaterialColor.GREEN;
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (entityIn.isSneaking()) {
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        } else {
            entityIn.onLivingFall(fallDistance, 0.0F);
        }
    }

    @Override
    public void onLanded(IBlockReader world, Entity entity) {
        if (entity.isSneaking()) {
            super.onLanded(world, entity);
        } else if (entity.getMotion().y < 0.0D) {
            entity.setMotion(entity.getMotion().x, -entity.getMotion().y, entity.getMotion().z);
        }
    }
}
