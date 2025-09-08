package spring.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;

import spring.dto.GroupDto;
import spring.dto.ScheduleItemDto;
import spring.dto.SubjectDto;
import spring.service.GroupService;
import spring.service.ScheduleItemService;
import spring.service.SubjectService;

public class ScheduleItemControllerUnitTest {
	
	private ScheduleItemController scheduleItemController;
	private ScheduleItemService scheduleItemService;
	private GroupService groupService;
	private SubjectService subjectService;
	
	@BeforeEach
	void setup() {
		scheduleItemService = mock(ScheduleItemService.class);
		groupService = mock(GroupService.class);
		subjectService = mock(SubjectService.class);
		scheduleItemController = new ScheduleItemController(scheduleItemService, groupService, subjectService);
		
	}
	
	@Test
	void shouldReturnNewSearchPageOnSearchAndSetScheduleOnSearch() {
		var query = "TestQuery";
		var model = new ExtendedModelMap();
		var startTime1 = "11-12-2025T14:05";
		var endTime1 = "11-12-2025T15:05";
		var groupDto1 = new GroupDto(1, "Test1", 5);
		var subjectDto1 = new SubjectDto(1, "Math", 5);
		var startTime2 = "10-11-2025T11:10";
		var endTime2 = "10-11-2025T12:10";
		var groupDto2 = new GroupDto(1, "Test2", 5);
		var subjectDto2 = new SubjectDto(1, "History", 5);
		var scheduleDtos = List.of(new ScheduleItemDto(1, startTime1, endTime1, groupDto1, subjectDto1, 5),
				new ScheduleItemDto(2, startTime2, endTime2, groupDto2, subjectDto2, 5));
		when(scheduleItemService.findByGroupName(query)).thenReturn(scheduleDtos);
		
		var actualResult = scheduleItemController.searchSchedules(query, model);
		
		assertThat(actualResult).isEqualTo("schedule/search");
		assertThat(model.getAttribute("scheduleDtos")).isEqualTo(scheduleDtos);
		verify(scheduleItemService, times(1)).findByGroupName(query);
		
	}
	
	@Test
	void shouldReturnAllSchedulesPageAndFindAllSchedulesAndSetGroupsSubjectsOnFindAll() {
		var model = new ExtendedModelMap();
		var startTime1 = "11-12-2025T14:05";
		var endTime1 = "11-12-2025T15:05";
		var groupDto1 = new GroupDto(1, "Test1", 5);
		var subjectDto1 = new SubjectDto(1, "Math", 5);
		var startTime2 = "10-11-2025T11:10";
		var endTime2 = "10-11-2025T12:10";
		var groupDto2 = new GroupDto(1, "Test2", 5);
		var subjectDto2 = new SubjectDto(1, "History", 5);
		var scheduleDtos = List.of(new ScheduleItemDto(1, startTime1, endTime1, groupDto1, subjectDto1, 5),
				new ScheduleItemDto(2, startTime2, endTime2, groupDto2, subjectDto2, 5));
		when(scheduleItemService.findAll()).thenReturn(scheduleDtos);
		
		var actualResult = scheduleItemController.findAll(model);
		
		assertThat(actualResult).isEqualTo("schedule/findAll");
		assertThat(model.getAttribute("schedules")).isEqualTo(scheduleDtos);
		assertThat(model.containsAttribute("allGroups")).isTrue();
		assertThat(model.containsAttribute("allSubjects")).isTrue();
		verify(scheduleItemService, times(1)).findAll();
		
	}
	
	@Test
	void shouldReturnNewDateFormAndSetSchedulesAndDatesAndGroupSubjects() {
		var startTime1 = "2025-11-11";
		var endTime1 = "2025-11-11";
		var groupDto1 = new GroupDto(1, "Test1", 5);
		var subjectDto1 = new SubjectDto(1, "Math", 5);
		var startTime2 = "2025-11-11";
		var endTime2 = "2025-11-11";
		var groupDto2 = new GroupDto(1, "Test2", 5);
		var subjectDto2 = new SubjectDto(1, "History", 5);
		var scheduleDtos = List.of(new ScheduleItemDto(1, startTime1, endTime1, groupDto1, subjectDto1, 5),
				new ScheduleItemDto(2, startTime2, endTime2, groupDto2, subjectDto2, 5));
		var localDate = LocalDate.of(2025, 11, 11);
		var model = new ExtendedModelMap();
		when(scheduleItemService.findAllWithDetails()).thenReturn(scheduleDtos);
		when(scheduleItemService.findAll()).thenReturn(scheduleDtos);
		
		var actualResult = scheduleItemController.findByDate(localDate, model);
		
		assertThat(actualResult).isEqualTo("schedule/byDate");
		assertThat((List<ScheduleItemDto>) model.getAttribute("schedules")).hasSize(2);
		assertThat(model.getAttribute("schedules")).isEqualTo(scheduleDtos);
		assertThat(model.containsAttribute("allGroups")).isTrue();
		assertThat(model.containsAttribute("allSubjects")).isTrue();
		
	}
	
	@Test
	void shouldReturnNewDateFormAndReturnEmtyListWhenNoScheduleOnDate() {
		var localDate = LocalDate.of(2025, 11, 11);
		var model = new ExtendedModelMap();
		when(scheduleItemService.findAllWithDetails()).thenReturn(List.of());
	    when(groupService.findAll()).thenReturn(List.of());
	    when(subjectService.findAll()).thenReturn(List.of());
	    
		var actualResult = scheduleItemController.findByDate(localDate, model);
		
		assertThat(actualResult).isEqualTo("schedule/byDate");
		assertThat((List<?>) model.getAttribute("schedules")).isEmpty();
		assertThat(model.containsAttribute("allGroups")).isTrue();
		assertThat(model.containsAttribute("allSubjects")).isTrue();
		
	}
	
	@Test
	void shouldReturnNewEditPageAndSetScheduleAndGroupsSubjectsOnEdit() {
		var startTime1 = "2025-11-11";
		var endTime1 = "2025-11-11";
		var groupDto1 = new GroupDto(1, "Test1", 5);
		var subjectDto1 = new SubjectDto(1, "Math", 5);
		var scheduleItemDto = new ScheduleItemDto(1, startTime1, endTime1, groupDto1, subjectDto1, 5);
		var model = new ExtendedModelMap();
		when(scheduleItemService.findById(1)).thenReturn(scheduleItemDto);
		
		var actualResult = scheduleItemController.edit(1, model);
		
		assertThat(actualResult).isEqualTo("schedule/edit");
		assertThat(model.getAttribute("schedule")).isEqualTo(scheduleItemDto);
		assertThat(model.containsAttribute("groups")).isTrue();
		assertThat(model.containsAttribute("subjects")).isTrue();
		verify(scheduleItemService, times(1)).findById(1);
		
	}
	
	@Test
	void shouldRedirectToNewScheduleFormWhenUpdateSucceeds() {
		var groupDto = new GroupDto(1, "Test", 5);
		var subjectDto = new SubjectDto(1, "Math", 5);
		var scheduleItemDto = new ScheduleItemDto(1, "2025-11-11T14:50", "2025-11-11T15:50", groupDto, subjectDto, 5);
		var model = new ExtendedModelMap();
		var bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(false);
		
		var actualResult = scheduleItemController.update(scheduleItemDto, bindingResult, 1, model);
		
		assertThat(actualResult).isEqualTo("redirect:/schedule");
		verify(scheduleItemService, times(1)).update(scheduleItemDto, 1);
		
	}
	
	@Test
	void shouldReturnNewScheduleEditFormAndSetGroupsSubjectsWhenValidationFailesOnUpdate() {
		var groupDto = new GroupDto(1, "Test", 5);
		var subjectDto = new SubjectDto(1, "Math", 5);
		var scheduleItemDto = new ScheduleItemDto(1, "2025-11-11T14:50", "2025-11-11T15:50", groupDto, subjectDto, 5);
		var model = new ExtendedModelMap();
		var bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(true);
		
		var actualResult = scheduleItemController.update(scheduleItemDto, bindingResult, 1, model);
		
		assertThat(actualResult).isEqualTo("schedule/edit");
		assertThat(model.containsAttribute("groups")).isTrue();
		assertThat(model.containsAttribute("subjects")).isTrue();
		verify(scheduleItemService, times(0)).update(scheduleItemDto, 1);
		
	}
	

}
