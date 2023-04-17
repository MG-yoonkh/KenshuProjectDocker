package mg.recipe.instruction;

import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstructionForm {
    
    MultipartFile imgUrl1;
    MultipartFile imgUrl2;
    MultipartFile imgUrl3;

}