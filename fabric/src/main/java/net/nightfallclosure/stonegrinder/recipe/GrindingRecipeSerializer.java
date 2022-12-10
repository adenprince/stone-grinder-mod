package net.nightfallclosure.stonegrinder.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class GrindingRecipeSerializer extends CookingRecipeSerializer<GrindingRecipe> {
    private final int cookingTime;
    private final CookingRecipeSerializer.RecipeFactory<GrindingRecipe> recipeFactory;

    public GrindingRecipeSerializer(CookingRecipeSerializer.RecipeFactory<GrindingRecipe> recipeFactory,
                                    int cookingTime) {
        super(recipeFactory, cookingTime);
        this.cookingTime = cookingTime;
        this.recipeFactory = recipeFactory;
    }

    @Override
    public GrindingRecipe read(Identifier identifier, JsonObject jsonObject) {
        String group = JsonHelper.getString(jsonObject, "group", "");
        JsonElement ingredientJson = JsonHelper.hasArray(jsonObject, "ingredient") ?
                JsonHelper.getArray(jsonObject, "ingredient") :
                JsonHelper.getObject(jsonObject, "ingredient");
        Ingredient ingredient = Ingredient.fromJson(ingredientJson);
        ItemStack itemStack;
        if (jsonObject.get("result").isJsonObject()) {
            itemStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
        }
        else {
            String resultString = JsonHelper.getString(jsonObject, "result");
            Identifier resultIdentifier = new Identifier(resultString);
            itemStack = new ItemStack(Registry.ITEM.getOrEmpty(resultIdentifier).orElseThrow(() ->
                    new IllegalStateException("Item: " + resultString + " does not exist")));
        }
        float experience = JsonHelper.getFloat(jsonObject, "experience", 0.0F);
        int cookingTime = JsonHelper.getInt(jsonObject, "cookingtime", this.cookingTime);
        return this.recipeFactory.create(identifier, group, ingredient, itemStack, experience, cookingTime);
    }
}
