package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.MongoUserAction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoUserActionRepository extends MongoRepository<MongoUserAction, String> {
    // 사용자 번호를 기반으로 사용자 행동 로그를 찾는 메서드 (페이징 처리), 최신 순으로 정렬
    List<MongoUserAction> findByUserSeqOrderByTimestampDesc(int userSeq, Pageable pageable);
    // 사용자 번호를 기반으로 사용자 행동 로그를 찾는 메서드
    List<MongoUserAction> findByUserSeq(int userSeq);
}
