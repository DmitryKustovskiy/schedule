package spring.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import spring.dto.GroupDto;
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
	void shouldFindAllGroupsAndAddToModel() {
		List<GroupDto> groups = List.of
				(new GroupDto(1, "Leads", 5), new GroupDto(2, "Manchesters", 7));
		
		when(groupService.findAll()).thenReturn(groups);
		Model model = new ExtendedModelMap();
        String actualResult = groupController.findAll(model);

        assertThat(actualResult).isEqualTo("group/findAll");
        assertThat(model.getAttribute("groups")).isEqualTo(groups);
        verify(groupService, times(1)).findAll();
        
	}
	
	@Test
	void shouldFindGroupById() {
		var groupDto = new GroupDto(1, "TestGroup", 1);
		when(groupService.findById(1)).thenReturn(groupDto);
		
		ExtendedModelMap model = new ExtendedModelMap();
		String actualResult = groupController.findById(1, model);
		
		assertThat(actualResult).isEqualTo("group/findById");
		assertThat(model.getAttribute("group")).isEqualTo(groupDto);
		verify(groupService, times(1)).findById(1);
		
	}
	
	@Test
	void shouldReturnNewGroupFormView() {
		var groupDto = new GroupDto();
		String actualResult = groupController.create(groupDto);
		
		assertThat(actualResult).isEqualTo("group/new");
		
	}
	
}
