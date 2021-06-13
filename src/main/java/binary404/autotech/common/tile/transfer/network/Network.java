package binary404.autotech.common.tile.transfer.network;

import binary404.autotech.common.tile.transfer.pipe.Pipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public abstract class Network {

    protected final NetworkGraph graph = new NetworkGraph(this);
    private final String id;
    private BlockPos originPos;
    private boolean didDoInitialScan;

    public Network(BlockPos originPos, String id) {
        this.id = id;
        this.originPos = originPos;
    }

    public void setOriginPos(BlockPos originPos) {
        this.originPos = originPos;
    }

    public String getId() {
        return id;
    }

    public NetworkGraphScannerResult scanGraph(World world, BlockPos pos) {
        return graph.scan(world, pos);
    }

    public List<Destination> getDestinations(DestinationType type) {
        return graph.getDestinations(type);
    }

    public CompoundNBT writeToNbt(CompoundNBT tag) {
        tag.putString("id", id);
        tag.putLong("origin", originPos.toLong());

        return tag;
    }

    public void update(World world) {
        if (!didDoInitialScan) {
            didDoInitialScan = true;

            scanGraph(world, originPos);
        }

        graph.getPipes().forEach(Pipe::update);
    }

    public Pipe getPipe(BlockPos pos) {
        return graph.getPipes().stream().filter(p -> p.getPos().equals(pos)).findFirst().orElse(null);
    }

    public abstract void onMergedWith(Network mainNetwork);

    public abstract ResourceLocation getType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Network network = (Network) o;
        return id.equals(network.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
