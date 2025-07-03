package me.mocadev.chatserver.common.domain;

import java.time.LocalDateTime;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Getter;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-07-03
 **/
@Getter
@MappedSuperclass
public class BaseTimeEntity {

	@CreationTimestamp
	private LocalDateTime createdTime;
	@UpdateTimestamp
	private LocalDateTime updatedTime;
}
