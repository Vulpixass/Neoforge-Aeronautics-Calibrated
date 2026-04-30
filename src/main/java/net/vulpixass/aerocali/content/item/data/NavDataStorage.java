package net.vulpixass.aerocali.content.item.data;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;
import java.util.function.Supplier;

public record NavDataStorage<T>(DataComponentType<T> type, Supplier<T> defaultSupplier, Function<T, T> validator) {
    public T get(ItemStack stack) {
        return stack.has(type) ? stack.get(type) : defaultSupplier.get();
    }

    public T defaultValue() {
        return defaultSupplier.get();
    }

    public void set(ItemStack stack, T value) {
        stack.set(type, validator.apply(value));
    }

}
