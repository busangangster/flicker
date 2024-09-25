package com.flicker.user.user.application;

import com.flicker.user.common.exception.RestApiException;
import com.flicker.user.common.status.StatusCode;
import com.flicker.user.user.domain.UserConverter;
import com.flicker.user.user.domain.entity.User;
import com.flicker.user.user.dto.MovieSeqListDto;
import com.flicker.user.user.dto.UserRegisterDto;
import com.flicker.user.user.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public boolean deleteBookmarkMovie(Long userSeq, Long movieSeq) {

        return false;
    }

    @Override
    public boolean deleteUnlikeMovie(Long userSeq, Long movieSeq) {
        return false;
    }

    @Override
    public boolean registerBookmarkMovie(Long userSeq, Long movieSeq) {
        return false;
    }

    @Override
    public boolean registerUnlikeMovie(Long userSeq, Long movieSeq) {
        return false;
    }

    @Override
    public boolean deleteFavoriteMovie(Long userSeq, Long movieSeq) {
        Optional<User> byId = userRepository.findById(userSeq);
        if (byId.isPresent()) {
            User user = byId.get();
            user.deleteFavoriteMovie(movieSeq);
            return true;
        }
        return false;
    }

    @Override
    public boolean registerFavoriteMovie(Long userSeq, MovieSeqListDto dto) {

        Optional<User> byId = userRepository.findById(userSeq);
        if (byId.isPresent()) {
            User user = byId.get();
            user.addFavoriteMovie(dto);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean register(UserRegisterDto dto) {

        User findUser = userRepository.findByUserId(dto.getUserId());
        if(findUser != null) {
            throw new RestApiException(StatusCode.DUPLICATE_ID);
        }

        dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        User user = userConverter.registerDtoToUser(dto);
        System.out.println("저장 완료");
        userRepository.save(user);

        return false;
    }

    @Override
    @Transactional
    public boolean delete(Long userSeq) {

        Optional<User> byId = userRepository.findById(userSeq);
        if(byId.isPresent()) {
            byId.get().deleteUser();
            return true;
        }

        return false;
    }
}
