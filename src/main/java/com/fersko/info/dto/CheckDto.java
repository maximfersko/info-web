package com.fersko.info.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckDto extends BaseDto {
    private Long id;
    private PeerDto peerDto;
    private TaskDto taskDto;
    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckDto checkDto))
            return false;
        return Objects.equals(id, checkDto.id)
               && Objects.equals(peerDto, checkDto.peerDto)
               && Objects.equals(taskDto, checkDto.taskDto)
               && Objects.equals(date, checkDto.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, peerDto, taskDto, date);
    }

}
