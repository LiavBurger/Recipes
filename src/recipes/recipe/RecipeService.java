package recipes.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService (RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe findRecipeById(Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Recipe not found for id = " + id);
        return recipe.get();
    }

    public Recipe saveRecipe (Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

    public void updateRecipe(Recipe updatedRecipe, long id) {
        Recipe recipe = findRecipeById(id);
        recipe.setName(updatedRecipe.getName());
        recipe.setDescription(updatedRecipe.getDescription());
        recipe.setIngredients(updatedRecipe.getIngredients());
        recipe.setDirections(updatedRecipe.getDirections());
        recipe.setCategory(updatedRecipe.getCategory());
        recipe.setDate(LocalDateTime.now());
        recipeRepository.save(recipe);
    }

    public List<Recipe> findByCategory(String name) {
        return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(name);
    }

    public List<Recipe> findByName(String name) {
        return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
    }
}