package com.fersko.info.mapper;

import com.fersko.info.dto.CheckDto;
import com.fersko.info.entity.Check;

public class CheckMapper implements BaseMapper<Check, CheckDto> {
	private static final PeerMapper peerMapper = new PeerMapper();
	private static final TaskMapper taskMapper = new TaskMapper();

	@Override
	public CheckDto toDto(Check entity) {
		if (entity == null) {
			return null;
		}
		return CheckDto.builder()
				.id(entity.getId())
				.peerDto(peerMapper.toDto(entity.getPeer()))
				.taskDto(taskMapper.toDto(entity.getTask()))
				.date(entity.getDate())
				.build();
	}

	@Override
	public Check fromDto(CheckDto dto) {
		if (dto == null) {
			return null;
		}
		return Check.builder()
				.id(dto.getId())
				.peer(peerMapper.fromDto(dto.getPeerDto()))
				.task(taskMapper.fromDto(dto.getTaskDto()))
				.date(dto.getDate())
				.build();
	}
}

