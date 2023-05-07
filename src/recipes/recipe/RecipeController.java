package recipes.recipe;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import recipes.user.User;
import recipes.user.UserDetailsImpl;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class RecipeController {
    private final RecipeService recipeService;
    private final RecipeToDTOMapper recipeToDTOMapper;

    Logger logger = LoggerFactory.getLogger(RecipeController.class);


    @Autowired
    public RecipeController(RecipeService recipeService, RecipeToDTOMapper recipeToDTOMapper) {
        this.recipeService = recipeService;
        this.recipeToDTOMapper = recipeToDTOMapper;
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<Map<String, Long>> postNewRecipe(@Valid @RequestBody Recipe recipe,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User author = new User();
        author.setEmail(userDetails.getUsername());
        author.setPassword(userDetails.getPassword());

        recipe.setAuthor(author);
        Recipe savedRecipe = recipeService.saveRecipe(recipe);

        Map<String, Long> response = new HashMap<>();
        response.put("id", savedRecipe.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable long id) {
        try {
            Recipe recipe = recipeService.findRecipeById(id);
            RecipeDTO response = recipeToDTOMapper.apply(recipe);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (HttpClientErrorException.NotFound exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<HttpStatus> deleteRecipe(@PathVariable long id,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Recipe recipe = recipeService.findRecipeById(id);

        if (recipe.getAuthor().getEmail().equals(userDetails.getUsername())) {
            recipeService.deleteRecipe(recipe);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<HttpStatus> updateRecipe(@Valid @RequestBody Recipe updatedRecipe,
                                                   @PathVariable long id,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Recipe recipe = recipeService.findRecipeById(id);

        if (recipe.getAuthor().getEmail().equals(userDetails.getUsername())) {
            recipeService.updateRecipe(updatedRecipe, id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/api/recipe/search")
    public ResponseEntity<List<RecipeDTO>> searchRecipes(@RequestParam Map<String, String> param) {
        if (param.size() == 1 && param.containsKey("category")) {
            List<RecipeDTO> recipeDTOs = recipeService.findByCategory(param.get("category")).stream().map(recipeToDTOMapper).toList();
            return new ResponseEntity<>(recipeDTOs, HttpStatus.OK);
        }
        if (param.size() == 1 && param.containsKey("name")) {
            List<RecipeDTO> recipeDTOs = recipeService.findByName(param.get("name")).stream().map(recipeToDTOMapper).toList();
            return new ResponseEntity<>(recipeDTOs, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}
