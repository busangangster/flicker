package com.flicker.user.user.domain.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Data
public class UserMovieId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "userSeq_123123")
    private User user;

//    private Long userSeq;
    private Long movieSeq;
}
