package binary404.autotech.common.tile.generator;

import binary404.autotech.common.block.generator.BlockBioGenerator;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.util.Counter;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileTiered;
import binary404.autotech.common.tile.util.IInventory;
import com.sun.scenario.effect.Crop;
import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class TileBioGenerator extends TileTiered<BlockBioGenerator> implements IInventory {
    protected final Counter burner;
    protected boolean generating;

    public TileBioGenerator(Tier tier) {
        this();
        this.tier = tier;
    }

    public TileBioGenerator() {
        super(ModTiles.bio_generator);
        this.inv.set(1);
        this.burner = new Counter(tier.genSpeed);
        this.burner.setTicks(0);
    }

    @Override
    public void readStorable(CompoundNBT nbt) {
        super.readStorable(nbt);
        this.burner.read(nbt, "burner");
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT nbt) {
        this.burner.write(nbt, "burner");
        return super.writeStorable(nbt);
    }

    @Override
    public void readSync(CompoundNBT nbt) {
        super.readSync(nbt);
        this.generating = nbt.getBoolean("generating");
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        nbt.putBoolean("generating", this.generating);
        return super.writeSync(nbt);
    }

    @Override
    protected int postTick(World world) {
        if (!isRemote() && checkRedstone()) {

            if(this.inv.getStackInSlot(0) != ItemStack.EMPTY && !this.energy.isFull() && !generating) {
                this.inv.getStackInSlot(0).shrink(1);
                this.burner.setTicks(this.tier.genSpeed);
                this.generating = true;
            }

            if (!this.burner.isEmpty() && !this.energy.isFull() && generating) {
                this.burner.back();
                this.energy.produce(this.tier.gen);
                sync(4);
            }

            if (this.burner.isEmpty()) {
                this.generating = false;
            }
        }
        return this.extractFromSides(world) > 0 ? 10 : -1;
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return slot == 0 && stack.getItem() instanceof BlockNamedItem && stack.getItem() != Items.REDSTONE;
    }

    @Override
    public boolean canExtract(int slot) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }

    public Counter getGenerator() {
        return this.burner;
    }

    public boolean isGenerating() {
        return this.generating;
    }
}
