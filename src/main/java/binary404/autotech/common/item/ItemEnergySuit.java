package binary404.autotech.common.item;

import binary404.autotech.AutoTech;
import binary404.autotech.common.core.logistics.EnergyItemWrapper;
import binary404.autotech.common.core.logistics.IEnergyContainerItem;
import binary404.autotech.common.core.util.Util;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.antlr.v4.runtime.misc.MultiMap;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ItemEnergySuit extends ArmorItem implements IEnergyContainerItem {

    int maxEnergy = 100000;
    int maxTransfer = 500;

    public ItemEnergySuit(IArmorMaterial materialIn, EquipmentSlotType slot) {
        super(materialIn, slot, new Item.Properties().group(AutoTech.group).maxDamage(0).setNoRepair());
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group)) {
            items.add(Util.setDefaultEnergyTag(new ItemStack(this, 1), 0));
            items.add(Util.setDefaultEnergyTag(new ItemStack(this, 1), maxEnergy));
        }
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        if (stack.getItem() == ModItems.jetpack)
            return "autotech:textures/model/jetpack.png";

        return stack.getItem() != ModItems.energy_leggings ? "autotech:textures/model/energy_armor.png" : "autotech:textures/model/energy_armor_layer_2.png";
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if (player.inventory.armorItemInSlot(2).getItem() == ModItems.energy_chestplate) {
            if (((ItemEnergySuit) ModItems.energy_chestplate).getEnergyStored(player.inventory.armorItemInSlot(2)) > 1) {
                ((ItemEnergySuit) ModItems.energy_chestplate).extractEnergy(player.inventory.armorItemInSlot(2), 1, false);
                player.addPotionEffect(new EffectInstance(Effects.HASTE, 100, 1, true, false));
            }
        }
        if (player.inventory.armorItemInSlot(3).getItem() == ModItems.energy_helmet) {
            if (((ItemEnergySuit) ModItems.energy_chestplate).getEnergyStored(player.inventory.armorItemInSlot(3)) > 1) {
                ((ItemEnergySuit) ModItems.energy_helmet).extractEnergy(player.inventory.armorItemInSlot(3), 1, false);
                player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 200, 0, true, false));
            }
        }
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.getTag() == null) {
            Util.setDefaultEnergyTag(stack, 0);
        }
        return MathHelper.clamp(1.0D - ((double) stack.getTag().getInt("Energy") / (double) getMaxEnergyStored(stack)), 0.0D, 1.0D);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0xD01010;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getEnergyStored(stack) > 0;
    }

    public int getEnergyStored(ItemStack stack) {
        if (stack.getTag() == null) {
            Util.setDefaultEnergyTag(stack, 0);
        }
        return Math.min(stack.getTag().getInt("Energy"), getMaxEnergyStored(stack));
    }

    public int getMaxEnergyStored(ItemStack stack) {
        return maxEnergy;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment.equals(Enchantments.UNBREAKING)) {
            return enchantment.equals(Enchantments.UNBREAKING);
        }
        return enchantment.type.canEnchantItem(this);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        extractEnergy(stack, amount * 100, false);
        return 0;
    }

    public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {
        if (stack.getTag() == null) {
            Util.setDefaultEnergyTag(stack, 0);
        }
        int stored = Math.min(stack.getTag().getInt("Energy"), getMaxEnergyStored(stack));
        int receive = Math.min(maxReceive, Math.min(getMaxEnergyStored(stack) - stored, maxTransfer));

        if (!simulate) {
            stored += receive;
            stack.getTag().putInt("Energy", stored);
        }
        return receive;
    }

    public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate) {
        if (stack.getTag() == null) {
            Util.setDefaultEnergyTag(stack, 0);
        }
        int stored = Math.min(stack.getTag().getInt("Energy"), getMaxEnergyStored(stack));
        int extract = Math.min(maxExtract, stored);

        if (!simulate) {
            stored -= extract;
            stack.getTag().putInt("Energy", stored);
        }
        return extract;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new EnergyItemWrapper(stack, this);
    }
}
