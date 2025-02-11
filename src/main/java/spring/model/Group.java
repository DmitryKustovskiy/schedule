package spring.model;

import java.util.List;

import lombok.Getter;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component
public class Group {
    private int id;
    private String name;
    private List<Student> students;
    private List<ScheduleItem> schedules;

    public Group(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
