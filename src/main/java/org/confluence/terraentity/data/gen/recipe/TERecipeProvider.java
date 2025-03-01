package org.confluence.terraentity.data.gen.recipe;


import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TEItems;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class TERecipeProvider extends RecipeProvider {

    public TERecipeProvider(PackOutput output) {
        super(output);
    }


    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeOutput) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TEItems.HORNET_STAFF.get())
                .pattern("BAB")
                .pattern(" C ")
                .pattern(" C ")
                .define('A', Items.BEE_SPAWN_EGG)
                .define('B', Items.HONEY_BLOCK)
                .define('C', ItemTags.FLOWERS)
                .unlockedBy("has_bee_spawn_egg",has(Items.BEE_SPAWN_EGG))
                .save(recipeOutput);

    }

}
