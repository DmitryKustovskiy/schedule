package spring.model;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component
public class Subject {
	private int id;
	private String name;
	
	public Subject(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
