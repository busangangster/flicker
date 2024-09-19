package com.flicker.user.user.domain.entity;

import com.flicker.user.common.exception.RestApiException;
import com.flicker.user.common.status.StatusCode;
import com.flicker.user.common.util.HashUtil;
import com.flicker.user.user.domain.UserGrade;
import com.flicker.user.user.domain.vo.Role;
import com.flicker.user.user.dto.MovieSeqListDto;
import com.flicker.user.user.dto.UserAndMovieIdDto;
import com.flicker.user.user.dto.UserUpdateDto;
import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.NoSuchElementException;

@Entity
@Builder
@AllArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    private String nickname;
    private String email;
    private String hashedPass;
    private LocalDate birthDate;
    private Character gender;
    private String profilePhotoUrl;

    @OneToMany(mappedBy = "userMovieId.userSeq")
    private List<FavoriteMovie> favoriteMovies;

    @OneToMany(mappedBy = "userMovieId.userSeq")
    private List<BookmarkMovie> bookmarkMovies;

    @OneToMany(mappedBy = "userMovieId.userSeq")
    private List<UnlikeMovie> unlikeMovies;

    @Embedded
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer isActive;

    public void addFavoriteMovie(MovieSeqListDto dto){

        for(Long movieSeq : dto.getMovieIdList()){

//            if(favoriteMovies.con)

            FavoriteMovie favoriteMovie =  new FavoriteMovie(new UserAndMovieIdDto(movieSeq, this.userSeq));
            favoriteMovies.add(favoriteMovie);
        }
    }

    public void deleteUser(){
        this.isActive = 0;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateUser(UserUpdateDto dto){
        this.nickname = dto.getNickname();
        this.email = dto.getEmail();
        this.hashedPass = HashUtil.sha256(dto.getPassword());
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getAge(){
        if(this.birthDate == null){
            throw new NoSuchElementException();
        }
        LocalDate now = LocalDate.now();
        return Period.between(this.birthDate, now).getYears();
    }

    protected User() {
    }

    public User(LocalDate birthDate, String email, Character gender, String hashedPass, String nickname) {
        if(birthDate == null || email == null || gender == null || hashedPass == null || nickname == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL,null);
        }
        this.birthDate = birthDate;
        this.email = email;
        this.gender = gender;
        this.hashedPass = HashUtil.sha256(hashedPass);
        this.nickname = nickname;
        this.role = new Role(UserGrade.USER);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isActive = 1;
    }
}
