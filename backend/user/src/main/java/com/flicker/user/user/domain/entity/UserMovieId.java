package com.flicker.user.user.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Data
public class UserMovieId implements Serializable {
    private Long userSeq;
    private Long movieSeq;
}
