package spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.model.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

}
