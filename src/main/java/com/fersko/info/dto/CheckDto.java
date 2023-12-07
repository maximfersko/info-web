package com.fersko.info.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckDto implements BaseDto<Long> {

    private Long id;
    private PeerDto peerDto;
    private TaskDto taskDto;
    private LocalDate date;

    public CheckDto() {
    }

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
