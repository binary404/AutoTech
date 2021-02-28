package binary404.autotech.common.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class BlockToxicFluid extends BlockBasicFlowingFluid {

    public BlockToxicFluid(Supplier<? extends FlowingFluid> fluidSupplier) {
        super(fluidSupplier);
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityCollision(state, worldIn, pos, entityIn);
        if (entityIn instanceof LivingEntity) {
            ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.POISON, 100, 5, false, true));
        }
    }
}
