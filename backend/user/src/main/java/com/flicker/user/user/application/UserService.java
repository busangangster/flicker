package com.flicker.user.user.application;

import com.flicker.user.user.domain.entity.User;
import com.flicker.user.user.dto.*;

import java.util.List;

public interface UserService {

    public boolean register(UserRegisterDto dto);
    public boolean delete(Integer userSeq);
    public UserLoginResDto update(Integer userSeq, UserUpdateDto dto);

    public User getUserByUserSeq(Integer userSeq);
    public String getNicknameByUserSeq(Integer userSeq);
    public MovieSeqListDto getFavoriteMovies(Integer userSeq);
    public MovieSeqListDto getUnlikeMovies(Integer userSeq);
    public MovieSeqListDto getBookmarkMovies(Integer userSeq);

    public boolean registerFavoriteMovie(Integer userSeq, MovieSeqListDto dto);
    public boolean registerUnlikeMovie(Integer userSeq, Integer movieSeq);
    public boolean registerBookmarkMovie(Integer userSeq, Integer movieSeq);

    public boolean deleteUnlikeMovie(Integer userSeq, Integer movieSeq);
    public boolean deleteBookmarkMovie(Integer userSeq, Integer movieSeq);

    public MovieDetail getMovieDetail(Integer userSeq, Integer movieSeq);
}
