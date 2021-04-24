package binary404.autotech.common.core.lib.multiblock;

import binary404.autotech.client.renders.core.ICubeRenderer;
import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.core.TileCore;
import binary404.autotech.common.tile.util.ITank;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class MultiblockPart extends TileCore implements IMultiblockPart {

    private BlockPos controllerPos;
    private MultiblockControllerBase controllerTile;
    protected Tier tier;

    public MultiblockPart(TileEntityType<?> type, Tier tier) {
        super(type);
        this.tier = tier;
        initializeInventory();
    }

    public MultiblockControllerBase getController() {
        if (getWorld() != null) {
            if (controllerTile == null && controllerPos != null) {
                setController((MultiblockControllerBase) world.getTileEntity(controllerPos));
            }
        }
        return controllerTile;
    }

    private void setController(MultiblockControllerBase controller1) {
        this.controllerTile = controller1;
        if (!getWorld().isRemote) {
            writeCustomData(100, writer -> {
                writer.writeBoolean(controllerTile != null);
                if (controllerTile != null) {
                    writer.writeBlockPos(controllerTile.getPos());
                }
            });
        }
    }

    @Override
    public void writeInitialSyncData(PacketBuffer buffer) {
        super.writeInitialSyncData(buffer);
        MultiblockControllerBase controller = getController();
        buffer.writeBoolean(controller != null);
        if (controller != null) {
            buffer.writeBlockPos(controller.getPos());
        }
    }

    @Override
    public void receiveInitialSyncData(PacketBuffer buffer) {
        super.receiveInitialSyncData(buffer);
        if (buffer.readBoolean()) {
            this.controllerPos = buffer.readBlockPos();
            this.controllerTile = null;
        }
    }

    @Override
    public void receiveCustomData(int discriminator, PacketBuffer buffer) {
        super.receiveCustomData(discriminator, buffer);
        if (discriminator == 100) {
            if (buffer.readBoolean()) {
                this.controllerPos = buffer.readBlockPos();
                this.controllerTile = null;
            } else {
                this.controllerPos = null;
                this.controllerTile = null;
            }
            scheduleChunkForRenderUpdate();
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("Tier", this.tier.ordinal());
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        this.tier = Tier.values()[nbt.getInt("Tier")];
        super.read(state, nbt);
    }

    @Override
    public void onRemoved(World world, BlockState state, BlockState newState, boolean isMoving) {
        super.onRemoved(world, state, newState, isMoving);
        if (controllerTile != null) {
            controllerTile.invalidateStructure();
        }
    }

    @Override
    public void addToMultiBlock(MultiblockControllerBase controllerBase) {
        setController(controllerBase);
    }

    @Override
    public void removeFromMultiBlock(MultiblockControllerBase controllerBase) {
        setController(null);
    }

    @Override
    public boolean isAttachedToMultiBlock() {
        return getController() != null;
    }

    @Override
    public void renderTileEntity(CCRenderState renderState, IVertexOperation... pipeline) {
        getBaseTexture().render(renderState, Cuboid6.full, pipeline);
    }

    public ICubeRenderer getBaseTexture() {
        MultiblockControllerBase controller = getController();
        return controller == null ? Textures.CASINGS[tier.ordinal()] : controller.getBaseTexture(this);
    }
}
