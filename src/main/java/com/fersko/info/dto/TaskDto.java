package com.fersko.info.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto extends BaseDto {
	private Long id;
	private String pkTitle;
	private TaskDto parentTask;
	private Integer maxXp;


	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof TaskDto taskDto))
			return false;
		return Objects.equals(id, taskDto.id)
				&& Objects.equals(pkTitle, taskDto.pkTitle)
				&& Objects.equals(parentTask, taskDto.parentTask)
				&& Objects.equals(maxXp, taskDto.maxXp);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, pkTitle, parentTask, maxXp);
	}
}
