package spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

}
