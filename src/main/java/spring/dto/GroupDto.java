package spring.dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.model.ScheduleItem;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {

	private Integer id;

	@NotBlank(message = "You should enter name of the group")
	@Size(min = 2, message = "Minimum 2 symbols required")
	private String name;
	
	private Integer version;

}
