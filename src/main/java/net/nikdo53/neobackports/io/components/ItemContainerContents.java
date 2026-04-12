package net.nikdo53.neobackports.io.components;

import com.google.common.collect.Iterables;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Stream;

public class ItemContainerContents {

    private static final int NO_SLOT = -1;
    private static final int MAX_SIZE = 256;
    public static final ItemContainerContents EMPTY = new ItemContainerContents(NonNullList.create());
    public static final Codec<ItemContainerContents> CODEC = ItemContainerContents.Slot.CODEC
            .listOf()
            .xmap(ItemContainerContents::fromSlots, ItemContainerContents::asSlots);
    public static final StreamCodec<ItemContainerContents> STREAM_CODEC = ByteBufCodecs.ITEM_STACK
            .apply(ByteBufCodecs.list(256))
            .map(ItemContainerContents::new, p_331691_ -> p_331691_.items);
    private final NonNullList<ItemStack> items;
    private final int hashCode;

    private ItemContainerContents(NonNullList<ItemStack> items) {
        if (items.size() > 256) {
            throw new IllegalArgumentException("Got " + items.size() + " items, but maximum is 256");
        } else {
            this.items = items;
            this.hashCode = hashStackList(items);
        }
    }

    private ItemContainerContents(int size) {
        this(NonNullList.withSize(size, ItemStack.EMPTY));
    }

    private ItemContainerContents(List<ItemStack> items) {
        this(items.size());

        for (int i = 0; i < items.size(); i++) {
            this.items.set(i, items.get(i));
        }
    }

    public static int hashStackList(List<ItemStack> list) {
        int i = 0;

        for (ItemStack itemstack : list) {
            i = i * 31 + hashItemAndComponents(itemstack);
        }

        return i;
    }

    public static int hashItemAndComponents(@Nullable ItemStack stack) {
        if (stack != null) {
            int i = 31 + stack.getItem().hashCode();
            return 31 * i + stack.getOrCreateTag().hashCode();
        } else {
            return 0;
        }
    }


    private static ItemContainerContents fromSlots(List<ItemContainerContents.Slot> slots) {
        OptionalInt optionalint = slots.stream().mapToInt(ItemContainerContents.Slot::index).max();
        if (optionalint.isEmpty()) {
            return EMPTY;
        } else {
            ItemContainerContents itemcontainercontents = new ItemContainerContents(optionalint.getAsInt() + 1);

            for (ItemContainerContents.Slot itemcontainercontents$slot : slots) {
                itemcontainercontents.items.set(itemcontainercontents$slot.index(), itemcontainercontents$slot.item());
            }

            return itemcontainercontents;
        }
    }

    public static ItemContainerContents fromItems(List<ItemStack> items) {
        int i = findLastNonEmptySlot(items);
        if (i == -1) {
            return EMPTY;
        } else {
            ItemContainerContents itemcontainercontents = new ItemContainerContents(i + 1);

            for (int j = 0; j <= i; j++) {
                itemcontainercontents.items.set(j, items.get(j).copy());
            }

            return itemcontainercontents;
        }
    }

    private static int findLastNonEmptySlot(List<ItemStack> items) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (!items.get(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    private List<ItemContainerContents.Slot> asSlots() {
        List<ItemContainerContents.Slot> list = new ArrayList<>();

        for (int i = 0; i < this.items.size(); i++) {
            ItemStack itemstack = this.items.get(i);
            if (!itemstack.isEmpty()) {
                list.add(new ItemContainerContents.Slot(i, itemstack));
            }
        }

        return list;
    }

    public void copyInto(NonNullList<ItemStack> list) {
        for (int i = 0; i < list.size(); i++) {
            ItemStack itemstack = i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
            list.set(i, itemstack.copy());
        }
    }

    public ItemStack copyOne() {
        return this.items.isEmpty() ? ItemStack.EMPTY : this.items.get(0).copy();
    }

    public Stream<ItemStack> stream() {
        return this.items.stream().map(ItemStack::copy);
    }

    public Stream<ItemStack> nonEmptyStream() {
        return this.items.stream().filter(p_331322_ -> !p_331322_.isEmpty()).map(ItemStack::copy);
    }

    public Iterable<ItemStack> nonEmptyItems() {
        return Iterables.filter(this.items, p_331420_ -> !p_331420_.isEmpty());
    }

    public Iterable<ItemStack> nonEmptyItemsCopy() {
        return Iterables.transform(this.nonEmptyItems(), ItemStack::copy);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return other instanceof ItemContainerContents itemcontainercontents && listMatches(this.items, itemcontainercontents.items);
        }
    }

    public static boolean listMatches(List<ItemStack> list, List<ItemStack> other) {
        if (list.size() != other.size()) {
            return false;
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (!ItemStack.matches(list.get(i), other.get(i))) {
                    return false;
                }
            }

            return true;
        }
    }


    @Override
    public int hashCode() {
        return this.hashCode;
    }

    /**
     * Neo:
     * {@return the number of slots in this container}
     */
    public int getSlots() {
        return this.items.size();
    }

    /**
     * Neo: Gets a copy of the stack at a particular slot.
     *
     * @param slot The slot to check. Must be within [0, {@link #getSlots()}]
     * @return A copy of the stack in that slot
     * @throws UnsupportedOperationException if the provided slot index is out-of-bounds.
     */
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return this.items.get(slot).copy();
    }

    /**
     * Neo: Throws {@link UnsupportedOperationException} if the provided slot index is invalid.
     */
    private void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= getSlots()) {
            throw new UnsupportedOperationException("Slot " + slot + " not in valid range - [0," + getSlots() + ")");
        }
    }

    static record Slot(int index, ItemStack item) {
        public static final Codec<ItemContainerContents.Slot> CODEC = RecordCodecBuilder.create(
                p_331695_ -> p_331695_.group(
                                Codec.intRange(0, 255).fieldOf("slot").forGetter(ItemContainerContents.Slot::index),
                                ItemStack.CODEC.fieldOf("item").forGetter(ItemContainerContents.Slot::item)
                        )
                        .apply(p_331695_, ItemContainerContents.Slot::new)
        );
    }

}
