package binary404.autotech.common.tile.generator;

import binary404.autotech.common.block.generator.BlockSteamGenerator;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.util.Counter;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileTiered;
import binary404.autotech.common.tile.util.IInventory;
import binary404.autotech.common.tile.util.IRedstoneInteract;
import binary404.autotech.common.tile.util.ITank;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileSteamGenerator extends TileTiered implements IInventory, IRedstoneInteract, ITank {
    protected final Counter burner;
    protected boolean burning;

    public TileSteamGenerator() {
        this(Tier.LV);
    }

    public TileSteamGenerator(Tier tier) {
        super(ModTiles.bio_generator, tier);
        this.inv.set(1);
        this.tank.setCapacity(FluidAttributes.BUCKET_VOLUME * 16).setChange(() -> TileSteamGenerator.this.sync(10));
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
        this.burning = nbt.getBoolean("burning");
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        nbt.putBoolean("burning", this.burning);
        return super.writeSync(nbt);
    }

    @Override
    protected int postTick(World world) {
        super.postTick(world);
        if (!isRemote() && checkRedstone()) {
            boolean flag = false;
            if (this.burner.isEmpty()) {
                ItemStack stack = this.inv.getStackInSlot(0);
                if (!stack.isEmpty() && !this.tank.isEmpty()) {
                    int burnTime = ForgeHooks.getBurnTime(stack);
                    if (burnTime > 0) {
                        long perFuelTick = 200l;
                        this.burner.setAll(burnTime * perFuelTick);
                        if (stack.hasContainerItem()) {
                            this.inv.setStack(1, stack.getContainerItem());
                        } else {
                            stack.shrink(1);
                        }
                        this.tank.drain(10, IFluidHandler.FluidAction.EXECUTE);
                        sync(4);
                    }
                }
            }
            if (!this.burner.isEmpty()) {
                if (!this.energy.isFull()) {
                    long toProduce = Math.min(this.energy.getEmpty(), Math.min(getGeneration(), (long) this.burner.getTicks()));
                    this.energy.produce(toProduce);
                    this.burner.back(toProduce);
                    if (this.burner.isEmpty()) {
                        this.burner.setAll(0);
                    }
                    this.tank.drain(10, IFluidHandler.FluidAction.EXECUTE);
                    flag = true;
                    sync(4);
                }
            }
            if (this.burning != flag) {
                this.burning = flag;
                sync(4);
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
        return slot == 0 && ForgeHooks.getBurnTime(stack) > 0;
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
        return this.burning;
    }
}
