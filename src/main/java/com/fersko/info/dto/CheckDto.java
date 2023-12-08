package com.fersko.info.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckDto implements BaseDto<Long> {

    private Long id;
    private PeerDto peerDto;
    private TaskDto taskDto;
    private LocalDate date;

}
