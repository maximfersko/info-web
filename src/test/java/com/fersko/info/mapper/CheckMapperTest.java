package com.fersko.info.mapper;

import com.fersko.info.dto.CheckDto;
import com.fersko.info.dto.PeerDto;
import com.fersko.info.dto.TaskDto;
import com.fersko.info.entity.Check;
import com.fersko.info.entity.Peer;
import com.fersko.info.entity.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CheckMapperTest {

	private static final CheckMapper checkMapper = new CheckMapper();

	@Test
	void testToDtoWithNonNullValues() {
		Check check = createSampleCheck();
		CheckDto checkDto = checkMapper.toDto(check);

		assertNotNull(checkDto);
		assertEquals(check.getId(), checkDto.getId());
		assertNotNull(checkDto.getPeerDto());
		assertNotNull(checkDto.getTaskDto());
		assertEquals(check.getDate(), checkDto.getDate());
	}

	@Test
	void testToEntityWithNonNullValues() {
		CheckDto checkDto = createSampleCheckDto();
		Check check = checkMapper.fromDto(checkDto);

		assertNotNull(check);
		assertEquals(checkDto.getId(), check.getId());
		assertNotNull(check.getPeer());
		assertNotNull(check.getTask());
		assertEquals(checkDto.getDate(), check.getDate());
	}

	private Check createSampleCheck() {
		Peer peer = new Peer(2L, "nickname", LocalDate.parse("2000-01-01"));
		Task task = new Task(2L, "taskTitle", null, 100);
		return new Check(1L, peer, task, LocalDate.parse("2022-01-01"));
	}

	private CheckDto createSampleCheckDto() {
		PeerDto peer = new PeerDto(2L, "nickname", LocalDate.parse("2000-01-01"));
		TaskDto task = new TaskDto(2L, "taskTitle", null, 100);
		return new CheckDto(1L, peer, task, LocalDate.parse("2022-01-01"));
	}

}