package spring.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindingResult;

import spring.dto.GroupDto;
import spring.dto.StudentDto;
import spring.dto.StudentUpdateDto;
import spring.service.GroupService;
import spring.service.StudentService;

public class StudentControllerUnitTest {
	private StudentService studentService;
	private StudentController studentController;
	private GroupService groupService;

	@BeforeEach
	void setup() {
		studentService = mock(StudentService.class);
		groupService = mock(GroupService.class);
		studentController = new StudentController(studentService, groupService);
	}

	@Test
	void shouldFindAllStudentsOnFindAll() {
		var groupDto = new GroupDto(1, "Test", 5);
		var studentDtos = List.of(new StudentDto(1, "Ivan", "Ivanov", groupDto, 5),
				new StudentDto(2, "Petr", "Petrov", groupDto, 5));
		var model = new ExtendedModelMap();
		when(studentService.findAll()).thenReturn(studentDtos);

		var actualResult = studentController.findAll(model);
		
		assertThat(actualResult).isEqualTo("student/findAll");
		assertThat(model.getAttribute("students")).isEqualTo(studentDtos);
		verify(studentService, times(1)).findAll();

	}

	@Test
	void shouldFindStudentWhenFindById() {
		int studentId = 1;
		var groupDto = new GroupDto(1, "Test", 5);
		var studentDto = new StudentDto(studentId, "Ivan", "Ivanov", groupDto, 5);
		var model = new ExtendedModelMap();
		when(studentService.findById(studentId)).thenReturn(studentDto);
		when(groupService.findGroupByStudentGroupId(studentId)).thenReturn(groupDto);
		
		var actualResult = studentController.findById(studentId, model);

		assertThat(actualResult).isEqualTo("student/findById");
		assertThat(model.getAttribute("student")).isEqualTo(studentDto);
		assertThat(model.getAttribute("group")).isEqualTo(groupDto);
		verify(studentService, times(1)).findById(studentId);
		verify(groupService, times(1)).findGroupByStudentGroupId(studentId);

	}

	@Test
	void shouldReturnNewStudentFormAndAllGroupsWhenCreate() {
		int studentId = 1;
		var groupDto = new GroupDto(1, "Test", 5);
		var groupDtos = List.of(new GroupDto(2, "Test2", 5), new GroupDto(3, "Test3", 6));
		var studentDto = new StudentDto(studentId, "Ivan", "Ivanov", groupDto, 5);
		var model = new ExtendedModelMap();
		when(groupService.findAll()).thenReturn(groupDtos);

		var actualResult = studentController.create(studentDto, model);

		assertThat(actualResult).isEqualTo("student/new");
		assertThat(model.getAttribute("groups")).isEqualTo(groupDtos);
		verify(groupService, times(1)).findAll();

	}

	@Test
	void shouldRedirectToAllStudentsWhenSaveSucceeds() {
		int studentId = 1;
		var groupDto = new GroupDto(1, "Test", 5);
		var studentDto = new StudentDto(studentId, "Ivan", "Ivanov", groupDto, 5);
		var bindingResult = mock(BindingResult.class);
		var model = new ExtendedModelMap();
		when(bindingResult.hasErrors()).thenReturn(false);

		var actualResult = studentController.save(studentDto, bindingResult, model);

		assertThat(actualResult).isEqualTo("redirect:/students");
		verify(studentService, times(1)).save(studentDto);

	}

	@Test
	void shouldReturnNewStudentFormAndSetErrorMessageWhenValidationFails() {
		var studentDto = new StudentDto();
		var groupDtos = List.of(new GroupDto(2, "Test2", 5), new GroupDto(3, "Test3", 6));
		var bindingResult = mock(BindingResult.class);
		var model = new ExtendedModelMap();
		when(bindingResult.hasErrors()).thenReturn(true);
		when(groupService.findAll()).thenReturn(groupDtos);

		var actualResult = studentController.save(studentDto, bindingResult, model);

		assertThat(actualResult).isEqualTo("student/new");
		assertThat(model.getAttribute("groups")).isEqualTo(groupDtos);
		verifyNoInteractions(studentService);
		verify(groupService, times(1)).findAll();

	}

	@Test
	void shouldReturnNewStudentChangeGroupFormOnChangeGroup() {
		int studentId = 1;
		var groupDto = new GroupDto(1, "Test", 5);
		var studentDto = new StudentDto(studentId, "Ivan", "Ivanov", groupDto, 5);
		var model = new ExtendedModelMap();
		var groupDtos = List.of(new GroupDto(2, "Test2", 5), new GroupDto(3, "Test3", 6));
		when(studentService.findById(studentId)).thenReturn(studentDto);
		when(groupService.findAll()).thenReturn(groupDtos);
		when(groupService.findGroupByStudentGroupId(studentId)).thenReturn(groupDto);
		
		var actualResult = studentController.changeGroup(studentId, model);

		assertThat(actualResult).isEqualTo("student/changeGroup");
		assertThat(model.getAttribute("studentDto")).isEqualTo(studentDto);
		assertThat(model.getAttribute("groups")).isEqualTo(groupDtos);
		assertThat(model.getAttribute("groupDto")).isEqualTo(groupDto);
		verify(studentService, times(1)).findById(studentId);
		verify(groupService, times(1)).findAll();
		verify(groupService, times(1)).findGroupByStudentGroupId(studentId);

	}

	@Test
	void shouldRedirectToAllStudentsOnSetGroupSucceeds() {
		int studentId = 1;
		var studentDto = new StudentDto();
		var actualResult = studentController.updateGroup(studentDto, studentId);

		assertThat(actualResult).isEqualTo("redirect:/students");
		verify(studentService, times(1)).setGroup(studentDto, studentId);

	}

	@Test
	void shouldReturnStudentEditFormOnEdit() {
		int studentId = 1;
		var groupDto = new GroupDto(1, "Test", 5);
		var studentDto = new StudentDto(studentId, "Ivan", "Ivanov", groupDto, 5);
		var model = new ExtendedModelMap();
		when(studentService.findById(studentId)).thenReturn(studentDto);

		var actualResult = studentController.edit(model, studentId);

		assertThat(actualResult).isEqualTo("student/edit");
		assertThat(model.getAttribute("student")).isEqualTo(studentDto);
		verify(studentService, times(1)).findById(studentId);

	}

	@Test
	void shouldReirectToAllStudentsOnUpdateSucceeds() {
		int studentId = 1;
		var studentDto = new StudentUpdateDto();
		var bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(false);

		var actualResult = studentController.update(studentDto, bindingResult, studentId);

		assertThat(actualResult).isEqualTo("redirect:/students");
		verify(studentService, times(1)).update(studentDto, studentId);

	}

	@Test
	void shouldReturnStudentEditFormWhenValidationFailsOnUpdate() {
		int studentId = 1;
		var studentDto = new StudentUpdateDto();
		var bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(true);

		var actualResult = studentController.update(studentDto, bindingResult, studentId);

		assertThat(actualResult).isEqualTo("student/edit");
		verify(studentService, times(0)).update(studentDto, studentId);

	}

	@Test
	void shouldRedirectToAllStudentsOnDeleteSucceed() {
		int studentId = 1;
		var actualResult = studentController.delete(studentId);

		assertThat(actualResult).isEqualTo("redirect:/students");

	}

}
