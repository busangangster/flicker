package com.flicker.user.user.application;

import com.flicker.user.user.dto.*;

public interface UserService {

    public boolean register(UserRegisterDto dto);
    public boolean delete(Long userSeq);
    public boolean registerFavoriteMovie(Long userSeq, MovieSeqListDto dto);
    public boolean deleteFavoriteMovie(Long userSeq, Long movieSeq);
    public boolean registerUnlikeMovie(Long userSeq, Long movieSeq);
    public boolean deleteUnlikeMovie(Long userSeq, Long movieSeq);
    public boolean registerBookmarkMovie(Long userSeq, Long movieSeq);
    public boolean deleteBookmarkMovie(Long userSeq, Long movieSeq);
}
