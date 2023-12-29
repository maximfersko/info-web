package com.fersko.info.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Friend extends BaseEntity {
	private Long id;
	private Peer firstPeer;
	private Peer secondPeer;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Friend friend))
			return false;
		return Objects.equals(id, friend.id)
				&& Objects.equals(firstPeer, friend.firstPeer)
				&& Objects.equals(secondPeer, friend.secondPeer);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstPeer, secondPeer);
	}
}
