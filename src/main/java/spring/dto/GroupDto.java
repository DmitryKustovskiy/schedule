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
public class GroupDto {
	
	private int id;
	
	@NotBlank(message = "You should enter name of the group")
	private String name;

}
