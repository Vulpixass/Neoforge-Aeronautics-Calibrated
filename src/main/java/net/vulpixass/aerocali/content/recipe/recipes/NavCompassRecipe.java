package net.vulpixass.aerocali.content.recipe.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.vulpixass.aerocali.content.item.AerocaliItems;
import net.vulpixass.aerocali.content.item.custom.NavigationElementItem;
import net.vulpixass.aerocali.content.item.custom.TraverseBoardItem;
import net.vulpixass.aerocali.content.item.data.NavTargetData;
import net.vulpixass.aerocali.content.recipe.AerocaliRecipes;
import net.vulpixass.aerocali.data.AerocaliDataComponents;

import java.util.List;

public class NavCompassRecipe extends CustomRecipe {
    public NavCompassRecipe(CraftingBookCategory category) {
        super(category);
    }

    // Tells the Recipe if the Ingredients match
    @Override
    public boolean matches(CraftingInput input, Level level) {
        List items = input.items();
        int compassCount = 0;
        int boardCount = 0;
        for (int i = 0; i < input.size(); i++) {
            ItemStack item = (ItemStack) items.get(i);
            if (item.getItem() instanceof TraverseBoardItem) {
                boardCount++;
            } else if (item.getItem() instanceof NavigationElementItem) {
                compassCount++;
            } else if (!(item.getItem().getDefaultInstance().isEmpty())) {
                return false;
            }
        }
        return compassCount == 1 && boardCount == 1;
    }

    // Lets the Navigation Compass be crafted
    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack result = new ItemStack(AerocaliItems.NAVIGATION_ELEMENT.get());

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.getItem() instanceof TraverseBoardItem) {
                NavTargetData data = stack.get(AerocaliDataComponents.NAV_TARGET.get());

                result.set(AerocaliDataComponents.NAV_TARGET.get(), data);
                break;
            }
        }
        return result;
    }

    // Sets the Remaining Items duh
    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (stack.getItem() instanceof TraverseBoardItem) {
                remaining.set(i, stack.copy());
            }
        }
        return remaining;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AerocaliRecipes.NAV_COMPASS_SERIALIZER.get();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }
}
