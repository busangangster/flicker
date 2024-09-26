package com.flicker.user.user.domain.entity;

import com.flicker.user.common.exception.RestApiException;
import com.flicker.user.common.status.StatusCode;
import com.flicker.user.user.domain.UserGrade;
import com.flicker.user.user.domain.vo.Role;
import com.flicker.user.user.domain.vo.UserInfo;
import com.flicker.user.user.dto.MovieSeqListDto;
import com.flicker.user.user.dto.UserUpdateDto;
import jakarta.persistence.*;
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
    private Integer userSeq;

    @Embedded
    private UserInfo userInfo;
    @Embedded
    private Role role;

    private String userId;
    private String nickname;
    private String email;
    private String hashedPass;
    private String profilePhotoUrl;

    // 선호 영화 관련
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteMovie> favoriteMovies;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookmarkMovie> bookmarkMovies;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnlikeMovie> unlikeMovies;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer isActive;

    // 리뷰 관련

    public boolean deleteBookmarkMovie(Integer movieSeq){
        return this.bookmarkMovies.removeIf(bookmarkMovie -> bookmarkMovie.getMovieSeq().equals(movieSeq));
    }

    public boolean deleteUnlikeMovie(Integer movieSeq){
        return this.unlikeMovies.removeIf(unlikeMovie -> unlikeMovie.getMovieSeq().equals(movieSeq));
    }

    public boolean addBookmarkMovie(Integer movieSeq){

        for(BookmarkMovie bookmarkMovie : this.bookmarkMovies){
            if(bookmarkMovie.getMovieSeq().equals(movieSeq)){
                return false;
            }
        }

        BookmarkMovie bookmarkMovie = new BookmarkMovie(movieSeq);
        bookmarkMovie.updateUser(this);
        this.bookmarkMovies.add(bookmarkMovie);
        return true;
    }

    public boolean addUnlikeMovie(Integer movieSeq){

        for(UnlikeMovie unlikeMovie : this.unlikeMovies){
            if(unlikeMovie.getMovieSeq().equals(movieSeq)){
                return false;
            }
        }

        UnlikeMovie unlikeMovie = new UnlikeMovie(movieSeq);
        unlikeMovie.updateUser(this);
        this.unlikeMovies.add(unlikeMovie);

        return true;
    }

    public void addFavoriteMovie(MovieSeqListDto dto){
        for(Integer movieSeq : dto.getMovieSeqList()){

            boolean isDuplicate = false;
            for(FavoriteMovie favoriteMovie : favoriteMovies){
                if(movieSeq.equals(favoriteMovie.getMovieSeq())){
                    isDuplicate = true;
                    break;
                }
            }
            if(!isDuplicate){
                System.out.println("저장 완료");
                FavoriteMovie favoriteMovie = new FavoriteMovie(movieSeq);
                favoriteMovie.updateUser(this);
                this.favoriteMovies.add(favoriteMovie);
            }
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
        this.hashedPass = dto.getPassword();
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getAge(){
        if(this.userInfo.getBirthDate() == null){
            throw new NoSuchElementException();
        }
        LocalDate now = LocalDate.now();
        return Period.between(this.userInfo.getBirthDate(), now).getYears();
    }

    protected User() {}

    public User(LocalDate birthDate, String email, Character gender, String hashedPass, String nickname, String userId) {
        if(birthDate == null || email == null || gender == null || hashedPass == null || nickname == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL,null);
        }

        this.userInfo = UserInfo
                .builder()
                .birthDate(birthDate)
                .gender(gender)
                .build();
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.hashedPass = hashedPass;
        this.role = new Role(UserGrade.USER);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isActive = 1;
    }
}
