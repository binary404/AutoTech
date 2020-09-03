package binary404.autotech.common.container.generator;

import binary404.autotech.common.container.ModContainers;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.container.slot.SlotBase;
import binary404.autotech.common.tile.generator.TileSteamGenerator;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class BioGeneratorContainer extends ContainerTile<TileSteamGenerator> {

    public BioGeneratorContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(ModContainers.bio_generator, id, inventory, buffer);
    }

    public BioGeneratorContainer(int id, PlayerInventory inventory, TileSteamGenerator te) {
        super(ModContainers.bio_generator, id, inventory, te);
    }

    public static BioGeneratorContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new BioGeneratorContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, TileSteamGenerator te) {
        super.init(inventory, te);
        addSlot(new SlotBase(te.getInventory(), 0, 82, 25));
        addPlayerInventory(inventory, 8, 84, 4);
    }
}
