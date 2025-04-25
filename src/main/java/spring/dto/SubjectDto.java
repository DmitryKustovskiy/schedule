package spring.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.model.ScheduleItem;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDto {
	private int id;
	private String name;

}
