package spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import spring.dto.GroupDto;
import spring.repository.GroupRepository;
import spring.repository.StudentRepository;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class GroupServiceIT {
	@Autowired
	private GroupService groupService;
	@Autowired
	private GroupRepository gr;
	@Autowired
	private StudentRepository sr;

	@BeforeEach
	void setup() {
		System.out.println("Groups" + gr.count());
		System.out.println("Students" + sr.count());
	}

	@AfterEach
	void setup2() {
		System.out.println("Groups" + gr.count());
		System.out.println("Students" + sr.count());
	}

	@Test
	void findAll() {
		List<GroupDto> dtos = groupService.findAll();
		assertEquals(3, dtos.size());
		assertThat(dtos)
		.extracting(GroupDto::getName)
		.containsExactlyInAnyOrder("Leads", "United", "Trinixy");

	}

	@Test
	void findById() {
		GroupDto firstGroup = groupService.findById(1);
		assertEquals("Leads", firstGroup.getName());

		GroupDto secondGroup = groupService.findById(2);
		assertEquals("United", secondGroup.getName());

		GroupDto thirdGroup = groupService.findById(3);
		assertEquals("Trinixy", thirdGroup.getName());

		assertFalse(firstGroup.getName().equals("fds"));
	}

}
