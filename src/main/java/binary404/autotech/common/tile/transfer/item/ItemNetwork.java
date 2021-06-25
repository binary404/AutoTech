package binary404.autotech.common.tile.transfer.item;

import binary404.autotech.AutoTech;
import binary404.autotech.common.tile.transfer.item.util.*;
import binary404.autotech.common.tile.transfer.network.Destination;
import binary404.autotech.common.tile.transfer.network.DestinationType;
import binary404.autotech.common.tile.transfer.network.Network;
import binary404.autotech.common.tile.transfer.network.NetworkGraphScannerResult;
import binary404.autotech.common.tile.transfer.pipe.Pipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemNetwork extends Network {

    public static final ResourceLocation TYPE = new ResourceLocation(AutoTech.modid, "item");

    private DestinationPathCache destinationPathCache;

    public ItemNetwork(BlockPos originPos, String id) {
        super(originPos, id);
    }

    @Override
    public NetworkGraphScannerResult scanGraph(World world, BlockPos pos) {
        NetworkGraphScannerResult result = super.scanGraph(world, pos);

        updateRouting(result, graph.getDestinations(DestinationType.ITEM_HANDLER));

        return result;
    }

    @Override
    public void onMergedWith(Network mainNetwork) {

    }

    @Override
    public ResourceLocation getType() {
        return TYPE;
    }

    private void updateRouting(NetworkGraphScannerResult result, List<Destination> destinations) {
        List<Node<BlockPos>> nodes = buildNodes(result.getFoundPipes());

        NodeIndex<BlockPos> nodeIndex = NodeIndex.of(nodes);

        EdgeFactory edgeFactory = new EdgeFactory(nodeIndex, result.getRequests());
        List<Edge<BlockPos>> edges = edgeFactory.create();

        Graph<BlockPos> graph = new Graph<>(nodes, edges);

        DestinationPathCacheFactory destinationPathCacheFactory = new DestinationPathCacheFactory(graph, nodeIndex, destinations);

        this.destinationPathCache = destinationPathCacheFactory.create();
    }

    private List<Node<BlockPos>> buildNodes(Set<Pipe> pipes) {
        return pipes.stream().map(p -> new Node<>(p.getPos())).collect(Collectors.toList());
    }

    public DestinationPathCache getDestinationPathCache() {
        return destinationPathCache;
    }

}
