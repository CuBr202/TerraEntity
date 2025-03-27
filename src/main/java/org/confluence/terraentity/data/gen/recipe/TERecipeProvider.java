package org.confluence.terraentity.data.gen.recipe;


import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import org.confluence.terraentity.init.TEItems;

import java.util.function.Consumer;


public class TERecipeProvider extends RecipeProvider {

    public TERecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeOutput) {

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TEItems.HORNET_STAFF.get())
                .pattern("BAB")
                .pattern(" C ")
                .pattern(" C ")
                .define('A', Items.BEE_SPAWN_EGG)
                .define('B', Items.HONEY_BLOCK)
                .define('C', ItemTags.FLOWERS)
                .unlockedBy("has_bee_spawn_egg",has(Items.BEE_SPAWN_EGG))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TEItems.LEATHER_WHIP.get())
                .pattern("  A")
                .pattern("AAA")
                .pattern("A  ")
                .define('A', Items.LEATHER)
                .unlockedBy("has_leather",has(Items.LEATHER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TEItems.BAMBOO_WHIP.get())
                .pattern("  A")
                .pattern("AAA")
                .pattern("A  ")
                .define('A', Items.BAMBOO)
                .unlockedBy("has_bamboo",has(Items.BAMBOO))
                .save(recipeOutput);
    }
}
