package com.flicker.user.user.application;

import com.flicker.user.user.dto.*;

import java.util.List;

public interface UserService {

    public boolean register(UserRegisterDto dto);
    public boolean delete(Long userSeq);

    public MovieSeqListDto getFavoriteMovies(Long userSeq);
    public MovieSeqListDto getUnlikeMovies(Long userSeq);
    public MovieSeqListDto getBookmarkMovies(Long userSeq);

    public boolean registerFavoriteMovie(Long userSeq, MovieSeqListDto dto);
    public boolean registerUnlikeMovie(Long userSeq, Long movieSeq);
    public boolean registerBookmarkMovie(Long userSeq, Long movieSeq);

    public boolean deleteUnlikeMovie(Long userSeq, Long movieSeq);
    public boolean deleteBookmarkMovie(Long userSeq, Long movieSeq);
}
