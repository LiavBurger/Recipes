package recipes.recipe;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;

import recipes.user.User;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Missing name")
    private String name;

    @NotBlank(message = "Missing description")
    private String description;

    @NotNull(message = "Missing ingredients")
    @ElementCollection
    @Size(min = 1)
    private List<String> ingredients;

    @NotNull(message = "Missing directions")
    @ElementCollection
    @Size(min = 1)
    private List<String> directions;

    @NotBlank(message = "Missing category ")
    private String category ;

    private LocalDateTime date = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

}