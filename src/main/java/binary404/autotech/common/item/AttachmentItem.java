package binary404.autotech.common.item;

import binary404.autotech.AutoTech;
import binary404.autotech.common.tile.transfer.attachment.AttachmentFactory;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class AttachmentItem extends Item {

    private final AttachmentFactory type;

    public AttachmentItem(AttachmentFactory type) {
        super(new Item.Properties().group(AutoTech.group));

        this.type = type;
    }

    public AttachmentFactory getFactory() {
        return type;
    }

}
