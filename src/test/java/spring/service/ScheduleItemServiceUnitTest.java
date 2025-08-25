package spring.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import jakarta.persistence.OptimisticLockException;
import spring.dto.ScheduleItemDto;
import spring.model.ScheduleItem;

public class ScheduleItemServiceUnitTest {

	private ScheduleItemService service = new ScheduleItemService(null, null, null, null);

	@Test
	void shouldReturnTrueWhenSubjectIdIsZero() {
		assertTrue(service.checkIfSubjectIsNull(0));

	}

	@Test
	void shouldReturnFalseifSubjectIdIsNotZero() {
		assertFalse(service.checkIfSubjectIsNull(5));

	}

	@Test
	void shouldReturnTrueWhenStartTimeIsNull() {
		assertTrue(service.checkIfStartTimeNull(null));

	}

	@Test
	void shouldReturnFalseWhenStartTimeIsNotNull() {
		assertFalse(service.checkIfStartTimeNull(LocalDateTime.now()));

	}

	@Test
	void shouldReturnTrueIfEndTimeIsNull() {
		assertTrue(service.checkIfEndTimeNull(null));

	}

	@Test
	void shouldReturnFalseWhenEndTimeIsNotNull() {
		assertFalse(service.checkIfEndTimeNull(LocalDateTime.now()));

	}

}
