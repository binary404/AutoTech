package binary404.autotech.common.tile.multiblock;

import binary404.autotech.client.fx.LaserRenderHandler;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.block.multiblock.BlockAssemblyLine;
import binary404.autotech.common.core.lib.multiblock.BlockPattern;
import binary404.autotech.common.core.lib.multiblock.FactoryBlockPattern;
import binary404.autotech.common.core.lib.multiblock.MultiblockAbility;
import binary404.autotech.common.core.lib.multiblock.MultiblockControllerBase;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.network.PacketHandler;
import binary404.autotech.common.network.PacketLaser;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.util.ITank;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileAssemblyLine extends MultiblockControllerBase<BlockAssemblyLine> implements ITank {

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
            MultiblockAbility.INPUT_ENERGY,
            MultiblockAbility.IMPORT_ITEMS,
            MultiblockAbility.EXPORT_ITEMS,
            MultiblockAbility.IMPORT_FLUIDS
    };

    public TileAssemblyLine(Tier tier) {
        super(ModTiles.assembly_line, tier);
        this.inv.set(17);
        this.tank.setCapacity(1000);
    }

    public TileAssemblyLine() {
        this(Tier.HV);
    }

    @Override
    protected void updateFormedValid() {

    }

    @Override
    protected int postTick(World world) {
        BlockPos pos = this.getPos();
        BlockPos pos2 = pos.offset(this.facing.getOpposite(), 3);
        pos2 = pos2.add(0, 2, 0);

        PacketHandler.sendToNearby(world, pos, new PacketLaser(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, pos2.getX() + 0.5, pos2.getY() + 0.5, pos2.getZ() + 0.5));

        return super.postTick(world);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("CCC", "CGC", "CGC", "#C#")
                .aisle("CCC", "G#G", "C#C", "#C#")
                .aisle("CCC", "G#G", "C#C", "#C#")
                .aisle("CCC", "G#G", "C#C", "#C#")
                .aisle("CSC", "CGC", "CGC", "#C#")
                .where('C', statePredicate(ModBlocks.mechanical_casing.getDefaultState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
                .where('G', statePredicate(ModBlocks.reinforced_glass.getDefaultState()))
                .where('#', isAirPredicate())
                .where('S', selfPredicate())
                .build();
    }
}
