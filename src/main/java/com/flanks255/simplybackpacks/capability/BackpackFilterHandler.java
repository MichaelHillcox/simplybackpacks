package com.flanks255.simplybackpacks.capability;

import com.flanks255.simplybackpacks.items.BackpackItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class BackpackFilterHandler extends ItemStackHandler {
    private BackpackItemHandler parent;

    public BackpackFilterHandler(BackpackItemHandler parent) {
        super(16);

        this.parent = parent;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!getStackInSlot(slot).isEmpty()) {
            this.setStackInSlot(slot, ItemStack.EMPTY);
            this.parent.dirty = true;
            return ItemStack.EMPTY;
        }

        return super.extractItem(slot, amount, simulate);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.hasTag()) {
            CompoundNBT tag = stack.getOrCreateTag();
            if (tag.contains("Items") || tag.contains("BlockEntityTag") || tag.contains("Inventory"))
                return stack;
        } else if (stack.getItem() instanceof BackpackItem || stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
            return stack;
        } else {
            this.setStackInSlot(slot, stack);
            this.parent.dirty = true;
            return stack;
        }

        return super.insertItem(slot, stack, simulate);
    }
}
