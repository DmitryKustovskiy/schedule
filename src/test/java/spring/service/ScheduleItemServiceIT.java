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

	private Group group1;

	private Subject subject1;
	
	private Group group2;

	private Subject subject2;

	@BeforeEach
	void setUp() {
		saveScheduleItems();
	}

	@Test
	void shouldFindAllSchedules() {
		var actualResult = scheduleItemService.findAll();
		assertEquals(2, actualResult.size());
		
	}
	
	@Test
	void shouldFindScheduleByGroupName() {
		var actualResult = scheduleItemService.findByGroupName("trinixy");
		
		assertThat(actualResult).isNotEmpty();
		assertThat(actualResult.get(0).getGroupDto().getName()).isEqualTo("Trinixy");
	    assertThat(actualResult.get(0).getSubjectDto().getName()).isEqualTo("History");
	    
	}
	
	@Test
	void shouldReturnEmptyListIfGroupWasNotFound() {
		var actualResult = scheduleItemService.findByGroupName("notExistedGroup");
		
		assertThat(actualResult).isEmpty();
		
	}
	
	@Test
	void shouldReturnEmptyListWhenGroupNameIsEmpty() {
	    var actualResult = scheduleItemService.findByGroupName("");
	    
	    assertThat(actualResult).isEmpty();
	}

	private void saveScheduleItems() {
		group1 = groupRepository.save(new Group("Leads"));
		subject1 = subjectRepository.save(new Subject("Math"));

		group2 = groupRepository.save(new Group("Trinixy"));
		subject2 = subjectRepository.save(new Subject("History"));
		
		firstScheduleItem = new ScheduleItem();
		firstScheduleItem.setStartTime(LocalDateTime.now());
		firstScheduleItem.setEndTime(LocalDateTime.now());
		firstScheduleItem.setGroup(group1);
		firstScheduleItem.setSubject(subject1);
		itemRepository.save(firstScheduleItem);
		
		secondScheduleItem = new ScheduleItem();
		secondScheduleItem.setStartTime(LocalDateTime.now());
		secondScheduleItem.setEndTime(LocalDateTime.now());
		secondScheduleItem.setGroup(group2);
		secondScheduleItem.setSubject(subject2);
		itemRepository.save(secondScheduleItem);

	}

}
