package recipes.recipe;


import java.time.LocalDateTime;
import java.util.List;

public record RecipeDTO (
        String name,
        String description,
        List<String> ingredients,
        List<String> directions,
        String category,
        LocalDateTime date
) {
}
