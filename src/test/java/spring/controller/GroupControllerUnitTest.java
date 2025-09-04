package spring.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import spring.dto.GroupDto;
import spring.exception.EntityAlreadyExistsException;
import spring.exception.GroupNotEmptyException;
import spring.service.GroupService;

public class GroupControllerUnitTest {

	private GroupService groupService;
	private GroupController groupController;

	@BeforeEach
	void setUp() {
		groupService = mock(GroupService.class);
		groupController = new GroupController(groupService);

	}

	@Test
	void shouldFindAllGroupsWhenFindAll() {
		var groupDtos = List.of(new GroupDto(1, "Leads", 5), new GroupDto(2, "Manchesters", 7));
		when(groupService.findAll()).thenReturn(groupDtos);

		var model = new ExtendedModelMap();
		var actualResult = groupController.findAll(model);

		assertThat(actualResult).isEqualTo("group/findAll");
		assertThat(model.getAttribute("groups")).isEqualTo(groupDtos);
		verify(groupService, times(1)).findAll();

	}

	@Test
	void shouldFindGroupWhenFindById() {
		var groupDto = new GroupDto(1, "TestGroup", 1);
		when(groupService.findById(1)).thenReturn(groupDto);

		var model = new ExtendedModelMap();
		var actualResult = groupController.findById(1, model);

		assertThat(actualResult).isEqualTo("group/findById");
		assertThat(model.getAttribute("group")).isEqualTo(groupDto);
		verify(groupService, times(1)).findById(1);

	}

	@Test
	void shouldReturnNewGroupFormWhenCreate() {
		var groupDto = new GroupDto();
		var actualResult = groupController.create(groupDto);

		assertThat(actualResult).isEqualTo("group/new");

	}

	@Test
	void shouldRedirectToGroupsWhenSaveSucceeds() {
		var groupDto = new GroupDto();
		var bindingResult = mock(BindingResult.class);
		var model = new ExtendedModelMap();
		when(bindingResult.hasErrors()).thenReturn(false);

		var actualResult = groupController.save(groupDto, bindingResult, model);

		assertThat(actualResult).isEqualTo("redirect:/groups");
		verify(groupService, times(1)).save(groupDto);

	}

	@Test
	void shouldReturnNewGroupFormWhenValidationFailsOnSave() {
		var groupDto = new GroupDto();
		var bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(true);

		var model = new ExtendedModelMap();
		var actualResult = groupController.save(groupDto, bindingResult, model);

		assertThat(actualResult).isEqualTo("group/new");
		verifyNoInteractions(groupService);

	}

	@Test
	void shouldReturnNewGroupFormAndSetErrorMessageWhenGroupExistsOnSave() {
		var groupDto = new GroupDto();
		var bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(false);
		doThrow(new EntityAlreadyExistsException("Group already exists")).when(groupService).save(groupDto);

		var model = new ExtendedModelMap();
		var actualResult = groupController.save(groupDto, bindingResult, model);

		assertThat(actualResult).isEqualTo("group/new");
		assertThat(model.getAttribute("errorMessage")).isEqualTo("Group already exists");
		verify(groupService, times(1)).save(groupDto);

	}

	@Test
	void shouldReturnEditFormWhenEditGroup() {
		int groupId = 1;
		var groupDto = new GroupDto(groupId, "TestGroup", 5);
		when(groupService.findById(1)).thenReturn(groupDto);

		var model = new ExtendedModelMap();
		var actualResult = groupController.edit(model, groupDto.getId());

		assertThat(actualResult).isEqualTo("group/edit");
		assertThat(model.getAttribute("group")).isEqualTo(groupDto);
		verify(groupService, times(1)).findById(groupId);

	}

	@Test
	void shouldRedirectToGroupsWhenUpdateSucceeds() {
		int groupId = 1;
		var groupDto = new GroupDto(groupId, "TestGroup", 5);
		var bindingResult = mock(BindingResult.class);
		var model = new ExtendedModelMap();
		when(bindingResult.hasErrors()).thenReturn(false);

		var actualResult = groupController.update(groupDto, bindingResult, groupDto.getId(), model);

		assertThat(actualResult).isEqualTo("redirect:/groups");
		verify(groupService, times(1)).update(groupDto, groupId);

	}

	@Test
	void shouldReturnEditFormWhenValidationFails() {
		var groupDto = new GroupDto(1, "TestGroup", 5);
		var bindingResult = mock(BindingResult.class);
		var model = new ExtendedModelMap();
		when(bindingResult.hasErrors()).thenReturn(true);

		var actualResult = groupController.update(groupDto, bindingResult, groupDto.getId(), model);

		assertThat(actualResult).isEqualTo("group/edit");
		verifyNoInteractions(groupService);

	}

	@Test
	void shouldReturnEditFormAndSetErrorMessageWhenGroupExistsOnUpdate() {
		int groupId = 7;
		var groupDto = new GroupDto(1, "TestGroup", 5);
		var bindingResult = mock(BindingResult.class);
		var model = new ExtendedModelMap();
		when(bindingResult.hasErrors()).thenReturn(false);

		doThrow(new EntityAlreadyExistsException("Group already exists")).when(groupService).update(groupDto, groupId);

		var actualResult = groupController.update(groupDto, bindingResult, groupId, model);

		assertThat(actualResult).isEqualTo("group/edit");
		assertThat(model.getAttribute("errorMessage")).isEqualTo("Group already exists");
		verify(groupService, times(1)).update(groupDto, groupId);

	}

	@Test
	void shouldRedirectToGroupsWhenDeleteSucceeds() {
		int groupId = 1;
		var model = new ExtendedModelMap();
		var actualResult = groupController.delete(groupId, model);

		assertThat(actualResult).isEqualTo("redirect:/groups");
		verify(groupService, times(1)).delete(groupId);

	}

	@Test
	void shouldReturnFindByIdPageAndSetErrorMessageWhenDeleteFails() {
		int groupId = 1;
		var model = new ExtendedModelMap();

		doThrow(new GroupNotEmptyException("Group is not empty")).when(groupService).delete(groupId);

		var actualResult = groupController.delete(groupId, model);

		assertThat(actualResult).isEqualTo("group/findById");
		assertThat(model.getAttribute("errorMessage")).isEqualTo("Group is not empty");
		verify(groupService, times(1)).delete(groupId);

	}

}
