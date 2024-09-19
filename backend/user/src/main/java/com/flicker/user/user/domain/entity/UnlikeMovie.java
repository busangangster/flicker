package com.flicker.user.user.domain.entity;

import com.flicker.user.user.dto.UserAndMovieIdDto;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@Getter
public class UnlikeMovie {

    @EmbeddedId
    private UserMovieId userMovieId;

    @ManyToOne
    @JoinColumn(name = "userSeq123123345")
    private User user;

    private LocalDateTime createAt;
    private Integer isActive;

    public void deleteBookmarkMovie(){
        this.isActive = 0;
    }

    protected UnlikeMovie() {
    }

    public UnlikeMovie(UserAndMovieIdDto dto){
        this.userMovieId = UserMovieId.builder()
                .userSeq(dto.getUserSeq())
                .movieSeq(dto.getMovieSeq())
                .build();
        this.isActive = 1;
        this.createAt = LocalDateTime.now();
    }
}
