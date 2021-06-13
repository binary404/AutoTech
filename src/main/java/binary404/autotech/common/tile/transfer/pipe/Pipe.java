package binary404.autotech.common.tile.transfer.pipe;

import binary404.autotech.common.tile.transfer.attachment.Attachment;
import binary404.autotech.common.tile.transfer.attachment.ServerAttachmentManager;
import binary404.autotech.common.tile.transfer.network.Network;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public abstract class Pipe {

    private final Logger logger = LogManager.getLogger(getClass());

    protected final World world;
    protected final BlockPos pos;
    protected final ServerAttachmentManager attachmentManager = new ServerAttachmentManager(this);

    protected Network network;

    public Pipe(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }

    public void update() {
        for (Attachment attachment : attachmentManager.getAttachments()) {
            attachment.update();
        }
    }

    public ServerAttachmentManager getAttachmentManager() {
        return attachmentManager;
    }

    public World getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Network getNetwork() {
        return network;
    }

    public void joinNetwork(Network network) {
        this.network = network;

        logger.debug(pos + " joined network " + network.getId());

        sendBlockUpdate();
    }

    public void leaveNetwork() {
        logger.debug(pos + " left network " + network.getId());

        this.network = null;

        sendBlockUpdate();
    }

    public void sendBlockUpdate() {
        BlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 1 | 2);
    }

    public CompoundNBT writeToNbt(CompoundNBT tag) {
        tag.putLong("pos", pos.toLong());

        attachmentManager.writeToNbt(tag);

        return tag;
    }

    public abstract ResourceLocation getId();

    public abstract ResourceLocation getNetworkType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pipe pipe = (Pipe) o;
        return world.equals(pipe.world) &&
                pos.equals(pipe.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, pos);
    }

}
