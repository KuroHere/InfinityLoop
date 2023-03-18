package infinityloop.features.modules.player;

import infinityloop.features.modules.Module;
import infinityloop.features.modules.ModuleCategory;
import infinityloop.util.utils.minecraft.InventoryUtil;
import net.minecraft.item.ItemExpBottle;

public class FastPlace
        extends Module {
    public FastPlace() {
        super("FastPlace", "Fast everything.", ModuleCategory.PLAYER);
    }

    @Override
    public void onUpdate() {
        if (FastPlace.fullNullCheck()) {
            return;
        }
        if (InventoryUtil.holdingItem(ItemExpBottle.class)) {
            FastPlace.mc.rightClickDelayTimer = 0;
        }
    }
}

