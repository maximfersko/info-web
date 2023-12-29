package com.fersko.info.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PeerDto extends BaseDto {
	private Long id;
	private String pkNickname;
	private LocalDate birthday;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof PeerDto peerDto))
			return false;
		return Objects.equals(id, peerDto.id)
				&& Objects.equals(pkNickname, peerDto.pkNickname)
				&& Objects.equals(birthday, peerDto.birthday);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, pkNickname, birthday);
	}
}
