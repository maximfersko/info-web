package com.fersko.info.dto;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PeerDto implements BaseDto<String> {

    private String id;
    private LocalDate birthday;

}
