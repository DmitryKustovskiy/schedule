package spring.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import spring.dto.ScheduleItemDto;
import spring.model.Group;
import spring.model.ScheduleItem;
import spring.model.Student;
import spring.model.Subject;
import spring.repository.GroupRepository;
import spring.repository.ScheduleItemRepository;
import spring.repository.StudentRepository;
import spring.repository.SubjectRepository;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ScheduleItemServiceIT {
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private ScheduleItemService scheduleItemService;
	@Autowired
	private ScheduleItemRepository itemRepository;
	@Autowired
	private SubjectRepository subjectRepository;

	private ScheduleItem firstScheduleItem;

	private ScheduleItem secondScheduleItem;

	private Group group;

	private Subject subject;

	@BeforeEach
	void setUp() {
		saveScheduleItems();
	}

	@Test
	void shouldFindAllSchedules() {
		List<ScheduleItemDto> allSchedules = scheduleItemService.findAll();
		assertEquals(2, allSchedules.size());
	}

	public void saveScheduleItems() {
		group = new Group("Leads");
		groupRepository.save(group);
		subject = new Subject("Math");
		subjectRepository.save(subject);

		firstScheduleItem = new ScheduleItem();
		firstScheduleItem.setStartTime(LocalDateTime.of(2025, 11, 12, 18, 20));
		firstScheduleItem.setEndTime(LocalDateTime.of(2025, 11, 12, 19, 20));
		firstScheduleItem.setGroup(group);
		firstScheduleItem.setSubject(subject);
		itemRepository.save(firstScheduleItem);

		secondScheduleItem = new ScheduleItem();
		secondScheduleItem.setStartTime(LocalDateTime.of(2025, 06, 10, 13, 30));
		secondScheduleItem.setEndTime(LocalDateTime.of(2025, 06, 10, 14, 30));
		secondScheduleItem.setGroup(group);
		secondScheduleItem.setSubject(subject);
		itemRepository.save(secondScheduleItem);
	}

}
