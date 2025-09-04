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
import org.springframework.validation.BindingResult;

import spring.dto.SubjectDto;
import spring.exception.EntityAlreadyExistsException;
import spring.service.SubjectService;

public class SubjectControllerUnitTest {

	private SubjectService subjectService;
	private SubjectController subjectController;

	@BeforeEach
	void setup() {
		subjectService = mock(SubjectService.class);
		subjectController = new SubjectController(subjectService);

	}

	@Test
	void shouldFindAllSubjectsWhenFindAll() {
		var subjectDtos = List.of(new SubjectDto(1, "Math", 5), new SubjectDto(2, "History", 5));
		when(subjectService.findAll()).thenReturn(subjectDtos);

		var model = new ExtendedModelMap();
		var actualResult = subjectController.findAll(model);

		assertThat(actualResult).isEqualTo("subject/findAll");
		assertThat(model.getAttribute("subjects")).isEqualTo(subjectDtos);
		verify(subjectService, times(1)).findAll();

	}

	@Test
	void shouldFindSubjectWhenFindById() {
		var subjectDto = new SubjectDto(1, "Math", 5);
		when(subjectService.findById(1)).thenReturn(subjectDto);

		var model = new ExtendedModelMap();
		var actualResult = subjectController.findById(1, model);

		assertThat(actualResult).isEqualTo("subject/findById");
		assertThat(model.getAttribute("subject")).isEqualTo(subjectDto);
		verify(subjectService, times(1)).findById(1);

	}

	@Test
	void shouldReturnNewSubjectFormWhenCreate() {
		var subjectDto = new SubjectDto();
		String actualResult = subjectController.create(subjectDto);

		assertThat(actualResult).isEqualTo("subject/new");

	}

	@Test
	void shouldRedirectToSubjectsWhenSaveSucceeds() {
		var subjectDto = new SubjectDto();
		var bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(false);

		var model = new ExtendedModelMap();
		var actualResult = subjectController.save(subjectDto, bindingResult, model);

		assertThat(actualResult).isEqualTo("redirect:/subjects");
		verify(subjectService, times(1)).save(subjectDto);

	}

	@Test
	void shouldReturnNewSubjectFormWhenValidationFailsOnSave() {
		var subjectDto = new SubjectDto();
		var bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(true);

		var model = new ExtendedModelMap();
		var actualResult = subjectController.save(subjectDto, bindingResult, model);

		assertThat(actualResult).isEqualTo("subject/new");
		verifyNoInteractions(subjectService);

	}

	@Test
	void shouldReturnNewSubjectFormAndSetErrorMessageWhenSubjectExistsOnSave() {
		var subjectDto = new SubjectDto();
		var bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(false);
		doThrow(new EntityAlreadyExistsException("Subject already exists")).when(subjectService).save(subjectDto);

		var model = new ExtendedModelMap();
		var actualResult = subjectController.save(subjectDto, bindingResult, model);

		assertThat(actualResult).isEqualTo("subject/new");
		assertThat(model.getAttribute("errorMessage")).isEqualTo("Subject already exists");
		verify(subjectService, times(1)).save(subjectDto);

	}

	@Test
	void shouldReturnEditFormWhenEdit() {
		int subjectId = 1;
		var subjectDto = new SubjectDto(subjectId, "Math", 5);
		var model = new ExtendedModelMap();
		when(subjectService.findById(1)).thenReturn(subjectDto);

		var actualResult = subjectController.edit(model, subjectId);

		assertThat(actualResult).isEqualTo("subject/edit");
		assertThat(model.getAttribute("subject")).isEqualTo(subjectDto);
		verify(subjectService, times(1)).findById(subjectId);

	}

	@Test
	void shouldRedirectToSubjectsWhenSucceedsOnUpdate() {
		int subjectId = 1;
		var subjectDto = new SubjectDto(subjectId, "Math", 5);
		var bindingResult = mock(BindingResult.class);
		var model = new ExtendedModelMap();
		when(bindingResult.hasErrors()).thenReturn(false);

		var actualResult = subjectController.update(subjectDto, bindingResult, subjectId, model);

		assertThat(actualResult).isEqualTo("redirect:/subjects");
		verify(subjectService, times(1)).update(subjectDto, subjectId);

	}

	@Test
	void shouldReturnSubjectEditPageWhenValidationFailsOnUpdate() {
		int subjectId = 1;
		var subjectDto = new SubjectDto(subjectId, "Math", 5);
		var model = new ExtendedModelMap();
		var bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(true);

		var actualResult = subjectController.update(subjectDto, bindingResult, subjectId, model);

		assertThat(actualResult).isEqualTo("subject/edit");
		verify(subjectService, times(0)).update(subjectDto, subjectId);

	}

	@Test
	void shouldReturnSubjectEditFormAndSetErrorMessageWhenSubjectExistsOnUpdate() {
		int subjectId = 1;
		var subjectDto = new SubjectDto(subjectId, "Math", 5);
		var model = new ExtendedModelMap();
		var bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(false);

		doThrow(new EntityAlreadyExistsException("Subject already exists")).when(subjectService).update(subjectDto,
				subjectId);

		var actualResult = subjectController.update(subjectDto, bindingResult, subjectId, model);

		assertThat(actualResult).isEqualTo("subject/edit");
		assertThat(model.getAttribute("errorMessage")).isEqualTo("Subject already exists");
		verify(subjectService, times(1)).update(subjectDto, subjectId);

	}

	@Test
	void shouldRedirectToSubjectsWhenDeleteSucceeds() {
		int subjectId = 1;
		var actualResult = subjectController.delete(subjectId);

		assertThat(actualResult).isEqualTo("redirect:/subjects");
		verify(subjectService, times(1)).delete(subjectId);

	}

}
