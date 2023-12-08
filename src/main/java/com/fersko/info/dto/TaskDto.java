package com.fersko.info.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto implements BaseDto<String> {

    private String id;
    private TaskDto parentTask;
    private Integer maxXp;

}
