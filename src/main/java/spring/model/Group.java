package spring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity
@Table(name = "class")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;

    public Group(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
