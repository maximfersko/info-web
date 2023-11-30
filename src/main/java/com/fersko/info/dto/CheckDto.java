package com.fersko.info.dto;

import com.fersko.info.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
public class CheckDto {

    private Integer id;

    private PeerDto peerDto;

    private TaskDto taskDto;

    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckDto)) return false;
        CheckDto checkDto = (CheckDto) o;
        return Objects.equals(id, checkDto.id) && Objects.equals(peerDto, checkDto.peerDto) && Objects.equals(taskDto, checkDto.taskDto) && Objects.equals(date, checkDto.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, peerDto, taskDto, date);
    }
}
