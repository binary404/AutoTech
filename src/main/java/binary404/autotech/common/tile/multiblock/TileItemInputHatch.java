package binary404.autotech.common.tile.multiblock;

import binary404.autotech.common.block.multiblock.BlockMultiBlock;
import binary404.autotech.common.core.lib.multiblock.IMultiblockAbilityPart;
import binary404.autotech.common.core.lib.multiblock.MultiBlockPart;
import binary404.autotech.common.core.lib.multiblock.MultiblockAbility;
import binary404.autotech.common.core.lib.multiblock.MultiblockControllerBase;
import binary404.autotech.common.core.logistics.*;
import binary404.autotech.common.core.logistics.energy.Energy;
import binary404.autotech.common.core.logistics.fluid.Tank;
import binary404.autotech.common.core.logistics.item.Inventory;
import binary404.autotech.common.core.logistics.item.ItemHandlerProxy;
import binary404.autotech.common.tile.ModTiles;
import net.minecraft.world.World;

import java.util.List;

public class TileItemInputHatch extends MultiBlockPart<BlockMultiBlock> implements IMultiblockAbilityPart<Inventory> {

    public TileItemInputHatch() {
        super(ModTiles.item_input_hatch, Tier.LV);
    }

    @Override
    protected int postTick(World world) {
        if (isAttachedToMultiBlock() && ticks % 40 == 0) {
            MultiblockControllerBase controller = getController();
            controller.sync(4);
            this.inv = new ItemHandlerProxy(controller.getInventory(), controller, true, false);
        }
        return -1;
    }

    @Override
    public void initializeInventory() {
        this.inv = new Inventory(0);
        this.energy = Energy.EMPTY;
        this.tank = new Tank(0);
    }

    @Override
    public void addToMultiBlock(MultiblockControllerBase controllerBase) {
        super.addToMultiBlock(controllerBase);
        this.inv = new ItemHandlerProxy(controllerBase.getInventory(), this.getController(), true, false);
    }

    @Override
    public void removeFromMultiBlock(MultiblockControllerBase controllerBase) {
        super.removeFromMultiBlock(controllerBase);
        this.inv = new Inventory(0);
    }

    @Override
    public MultiblockAbility<Inventory> getAbility() {
        return MultiblockAbility.IMPORT_ITEMS;
    }

    @Override
    public void registerAbilities(List<Inventory> abilityList) {
        abilityList.add(this.inv);
    }
}
