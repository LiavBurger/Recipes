package recipes.recipe;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class RecipeToDTOMapper implements Function<Recipe, RecipeDTO> {

    @Override
    public RecipeDTO apply(Recipe recipe) {
        return new RecipeDTO(recipe.getName(),
                recipe.getDescription(),
                recipe.getIngredients(),
                recipe.getDirections(),
                recipe.getCategory(),
                recipe.getDate());
    }
}
