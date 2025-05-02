package spring.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.model.ScheduleItem;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDto {
	
	private int id;
	
	@NotBlank(message = "You should enter name of the subject")
	private String name;

}
