package spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import spring.dto.GroupDto;
import spring.dto.ScheduleItemDto;
import spring.dto.SubjectDto;
import spring.mapper.GroupMapper;
import spring.mapper.ScheduleItemMapper;
import spring.mapper.SubjectMapper;
import spring.model.Group;
import spring.model.ScheduleItem;
import spring.model.Subject;
import spring.repository.GroupRepository;
import spring.repository.ScheduleItemRepository;
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
	private ScheduleItemRepository scheduleItemRepository;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private SubjectService subjectService;
	
	@Autowired
	private GroupMapper groupMapper;
	
	@Autowired
	private SubjectMapper subjectMapper;
	
	@Autowired
	ScheduleItemMapper itemMapper;
	
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
	void shouldReturnEmptyListForNoSchedules() {
		scheduleItemService.delete(firstScheduleItem.getId());
		scheduleItemService.delete(secondScheduleItem.getId());
		List<ScheduleItemDto> actualResult = scheduleItemService.findAll();
		
		assertThat(actualResult.isEmpty());
		
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
	void shouldThrowIllegalArgumentExceptionOnEmptyInput() {
	    assertThrows(IllegalArgumentException.class, 
	    		() -> scheduleItemService.findByGroupName(""));
	    
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionOnNullInput() {
	    assertThrows(IllegalArgumentException.class, 
	    		() -> scheduleItemService.findByGroupName(null));
	    
	}
	
	@Test
	void shouldFindUniqueDates() {
		ScheduleItem testScheduleItem = new ScheduleItem();
		testScheduleItem.setStartTime(secondScheduleItem.getStartTime());
		testScheduleItem.setEndTime(secondScheduleItem.getEndTime());
		testScheduleItem.setGroup(group1);
		testScheduleItem.setSubject(subject1);
		scheduleItemRepository.save(testScheduleItem);
		Set<LocalDate> actualResult = scheduleItemService.findAllUniqueDates();
		
		assertEquals(2, actualResult.size());
		
	}
	
	@Test
	void shouldFindAllScheduleItemsWithSubjectsAndGroups() {
		var actualResult = scheduleItemService.findAllWithDetails();
		
		assertEquals(2, actualResult.size());
		assertThat(actualResult).extracting(ScheduleItemDto::getGroupDto)
		.containsExactlyInAnyOrder(groupMapper.toDto(group1), groupMapper.toDto(group2));
		assertThat(actualResult).extracting(ScheduleItemDto::getSubjectDto)
		.containsExactlyInAnyOrder(subjectMapper.toDto(subject1), subjectMapper.toDto(subject2));
		
	}
	
	@Test
	void shouldfindScheduleItemById() {
		var actualResult = scheduleItemService.findById(firstScheduleItem.getId());
		
		assertThat(actualResult.getGroupDto().getName() == group1.getName());
		assertThat(actualResult.getSubjectDto().getName() == subject1.getName());
		
	}
	
	@Test
	void shouldThrowEntitytNotFoundExceptionIfScheduleItemNotFound() {
		int notExistedId = Integer.MAX_VALUE;
		
		assertThrows(EntityNotFoundException.class, 
				() -> scheduleItemService.findById(notExistedId));
		
	}
	
	@Test
	void shouldSaveScheduleItem() {
		var groupDto = new GroupDto();
		groupDto.setName("TestGroup");
		var savedGroup = groupService.save(groupDto);
		
		var subjectDto = new SubjectDto();
		subjectDto.setName("TestSubject");
		var savedSubject = subjectService.save(subjectDto);
		
		var testScheduleItem = new ScheduleItem();
		testScheduleItem.setStartTime(LocalDateTime.now());
		testScheduleItem.setEndTime(LocalDateTime.now());
		testScheduleItem.setGroup(savedGroup);
		testScheduleItem.setSubject(savedSubject);
		ScheduleItemDto dtoSchedule = itemMapper.toDto(testScheduleItem);
		var actualResult = scheduleItemService.save(dtoSchedule);
		
		assertNotNull(actualResult.getId());
		assertNotNull(actualResult.getStartTime());
		assertNotNull(actualResult.getEndTime());
		assertThat(actualResult.getGroup()).isEqualTo(savedGroup);
		assertThat(actualResult.getSubject()).isEqualTo(savedSubject);
		
	}
	
	@Test
	void shouldUpdateScheduleItem() {
		var testGroupDto = new GroupDto();
		testGroupDto.setName("TestGroup");
		var savedGroup = groupService.save(testGroupDto);
		
		var testSubjectDto = new SubjectDto();
		testSubjectDto.setName("TestSubject");
		var savedSubject = subjectService.save(testSubjectDto);
		
		ScheduleItem testScheduleItem = new ScheduleItem();
		testScheduleItem.setId(555);
		testScheduleItem.setStartTime(LocalDateTime.now());
		testScheduleItem.setEndTime(LocalDateTime.now());
		testScheduleItem.setGroup(savedGroup);
		testScheduleItem.setSubject(savedSubject);
		ScheduleItemDto testScheduleDto = itemMapper.toDto(testScheduleItem);
		
		ScheduleItemDto toBeUpdated = scheduleItemService.findById(firstScheduleItem.getId());
		testScheduleDto.setVersion(toBeUpdated.getVersion());
		ScheduleItem actualResult = scheduleItemService.update(testScheduleDto, toBeUpdated.getId());
		
		assertThat(testScheduleDto.getGroupDto().getName())
		.isEqualTo(actualResult.getGroup().getName());
		
	}
	
	@Test
	void shouldThrowOptimisticLockExceptionOnUpdate() {
		var testGroupDto = new GroupDto();
		testGroupDto.setName("TestGroup");
		var savedGroup = groupService.save(testGroupDto);
		
		var testSubjectDto = new SubjectDto();
		testSubjectDto.setName("TestSubject");
		var savedSubject = subjectService.save(testSubjectDto);
		
		var testScheduleItem = new ScheduleItem();
		testScheduleItem.setId(555);
		testScheduleItem.setStartTime(LocalDateTime.now());
		testScheduleItem.setEndTime(LocalDateTime.now());
		testScheduleItem.setGroup(savedGroup);
		testScheduleItem.setSubject(savedSubject);
		var testScheduleDto = itemMapper.toDto(testScheduleItem);
		
		ScheduleItemDto toBeUpdated = scheduleItemService.findById(firstScheduleItem.getId());
		testScheduleDto.setVersion(toBeUpdated.getVersion() -1);
		
		assertThrows(OptimisticLockException.class, 
				() ->  scheduleItemService.update(testScheduleDto, toBeUpdated.getId()));
	
	}

	@Test
	void shouldDeleteScheduleItem() {
		scheduleItemService.delete(firstScheduleItem.getId());
		List<ScheduleItemDto> allSchedules = scheduleItemService.findAll();
		
		assertEquals(1, allSchedules.size());
		assertThat(allSchedules.get(0).getGroupDto().getName())
		.isEqualTo(secondScheduleItem.getGroup().getName());
		
	}
	
	@Test
	void shouldThrowEntitytNotFoundExceptionOnDeleteIfScheduleWasNotFound() {
		int notExistedId = Integer.MAX_VALUE;
		
		assertThrows(EntityNotFoundException.class, 
				() -> scheduleItemService.delete(notExistedId));
		
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
		scheduleItemRepository.save(firstScheduleItem);
		
		secondScheduleItem = new ScheduleItem();
		secondScheduleItem.setStartTime(LocalDateTime.of(2020, 12, 03, 22, 45));
		secondScheduleItem.setEndTime(LocalDateTime.of(2020, 12, 03, 23, 45));
		secondScheduleItem.setGroup(group2);
		secondScheduleItem.setSubject(subject2);
		scheduleItemRepository.save(secondScheduleItem);

	}

}
