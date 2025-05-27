package spring.model;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public abstract class AuditingEntity {
	
	private Instant modifiedAt;
	private Instant createdAt;

}
