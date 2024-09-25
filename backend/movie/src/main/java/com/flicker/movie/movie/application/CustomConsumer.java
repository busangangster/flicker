package com.flicker.movie.movie.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.movie.common.module.exception.RestApiException;
import com.flicker.movie.common.module.status.StatusCode;
import com.flicker.movie.movie.config.KafkaConfig;
import com.flicker.movie.movie.domain.entity.MongoUserAction;
import com.flicker.movie.movie.domain.entity.Movie;
import com.flicker.movie.movie.domain.entity.WordCloud;
import com.flicker.movie.movie.dto.AlarmMovieEvent;
import com.flicker.movie.movie.dto.KeywordCount;
import com.flicker.movie.movie.dto.MovieRatingEvent;
import com.flicker.movie.movie.dto.WordCloudEvent;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

/*
     * CustomConsumer 클래스는 Kafka 메시지를 수신하는 컨슈머 역할을 한다.
     * KafkaConfig, MovieBuilderUtil, MovieRepoUtil, ObjectMapper를 주입받는다.
     * build() 메서드에서 KafkaConsumer 객체를 생성한다.
     * consumeMovieRating() 메서드는 영화 평점 업데이트 Kafka 메시지를 수신한다.
     * consumeWordCloud() 메서드는 영화 워드 클라우드 업데이트 Kafka 메시지를 수신한다.
     * consumeAlarmMovie() 메서드는 주기적 이벤트 처리 Kafka 메시지를 수신한다.
     * @RetryableTopic 어노테이션을 사용하여 메시지 수신 중 오류가 발생할 경우 재시도한다.
     * @KafkaListener 어노테이션을 사용하여 토픽을 구독한다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomConsumer {

    private final KafkaConfig config;
    private final MovieBuilderUtil movieBuilderUtil;
    @Getter
    @Setter
    private KafkaConsumer<String, String> consumer = null;
    private final ObjectMapper objectMapper;
    private final MovieRepoUtil movieRepoUtil;
    private final MovieService movieService;

    @PostConstruct
    public void build() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, config.getConsumer().getGroupId());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, config.getConsumer().getKeyDeserializer());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, config.getConsumer().getValueDeserializer());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, config.getConsumer().getAutoOffsetReset());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, config.getConsumer().getMaxPollRecords());
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, config.getConsumer().getEnableAutoCommit());
        // UTF-8 설정 추가
        properties.setProperty("key.deserializer.encoding", "UTF-8");
        properties.setProperty("value.deserializer.encoding", "UTF-8");
        consumer = new KafkaConsumer<>(properties);
    }

    // Kafka 메시지 수신 ( 영화 평점 업데이트 )
    @RetryableTopic(
            attempts = "5",
            backoff = @Backoff(delay = 2000)
    )
    @KafkaListener(topics = "${spring.kafka.template.movie-rating-topic}")
    @Transactional
    public void consumeMovieRating(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String payload) {
        try {
            // 1. 역직렬화: payload를 MovieRatingEvent 객체로 변환
            MovieRatingEvent movieRatingEvent = objectMapper.readValue(payload, MovieRatingEvent.class);
            // 2. 영화 ID와 평점 추출
            int movieSeq = movieRatingEvent.getMovieSeq();
            double movieRating = movieRatingEvent.getMovieRating();
            // 3. DB에서 해당 영화 조회
            Movie movie = movieRepoUtil.findById(movieSeq);
            // 4. 영화 평점 업데이트
            movie.updateMovieRating(movieRating);
            // 5. 오프셋 커밋
            consumer.commitSync();
        } catch (Exception e) {
            throw new RestApiException(StatusCode.KAFKA_ERROR, "Kafka 이벤트 수신 중 오류가 발생했습니다.");
        }
    }

    // Kafka 메시지 수진 ( 영화 워드 클라우드 업데이트 )
    @RetryableTopic(
            attempts = "5",
            backoff = @Backoff(delay = 2000)
    )
    @KafkaListener(topics = "${spring.kafka.template.wordcloud-result-topic}")
    @Transactional
    public void consumeWordCloud(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String payload) {
        try {
            // 1. 역직렬화: payload를 WordCloudEvent 객체로 변환
            WordCloudEvent wordCloudEvent = objectMapper.readValue(payload, WordCloudEvent.class);
            // 2. 영화 ID와 키워드 추출
            int movieSeq = wordCloudEvent.getMovieSeq();
            List<KeywordCount> keywordCounts = wordCloudEvent.getKeywordCounts();
            LocalDateTime createdAt = wordCloudEvent.getTimeStamp();
            // 3. DB에서 해당 영화 조회
            Movie movie = movieRepoUtil.findById(movieSeq);
            // 4. 기존 워드 클라우드 초기화
            movie.clearWordClouds();
            // 5. 영화 워드 클라우드 추가
            List<WordCloud> wordClouds = movieBuilderUtil.buildWordCloudList(keywordCounts, createdAt);
            movie.addWordClouds(wordClouds);
            // 6. 오프셋 커밋
            consumer.commitSync();
        } catch (Exception e) {
            throw new RestApiException(StatusCode.KAFKA_ERROR, "Kafka 이벤트 수신 중 오류가 발생했습니다.");
        }
    }

    // Kafka 메시지 수신 ( 주기적 이벤트 처리 ( 1일 TOP10 영화, 사용자 행동 제거 ) )
    @RetryableTopic(
            attempts = "5",
            backoff = @Backoff(delay = 2000)
    )
    @KafkaListener(topics = "${spring.kafka.template.alarm-movie-topic}")
    @Transactional
    public void consumeAlarmMovie(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String payload) {
        try {
            // 1. 역직렬화: payload를 AlarmMovieEvent 객체로 변환
            AlarmMovieEvent alarmMovieEvent = objectMapper.readValue(payload, AlarmMovieEvent.class);
            // 2. TYPE 체크
            if(alarmMovieEvent.getType().equals("Today")) {
                // 1. 모든 사용자의 최근 행동 로그 조회 ( 1일 )
                List<MongoUserAction> userActions = movieRepoUtil.findUserActionsForMongoDB();
                // 2. 사용자 행동 로그 중 영화조회이면서, 가장 빈도 수가 높은 키워드 TOP10 추출
                List<String> topKeywords = movieService.findTopKeywords(userActions);
                // 3. 해당 키워드의 영화 번호 추출
                List<Integer> movieSeqs = movieService.findMovieSeqsByKeywords(topKeywords);
                // 4. Redis에 영화 번호 목록을 저장
                movieService.saveTopMovieForRedis(movieSeqs);
            }
            else if(alarmMovieEvent.getType().equals("ActionDelete")) {
                // 1. MongoDB에서 오래된 사용자 행동 제거 (1주일)
                movieRepoUtil.deleteUserActionsForMongoDB();
            } else {
                throw new RestApiException(StatusCode.KAFKA_ERROR, alarmMovieEvent + ": Kafka alarm-movie-topic의 type이 올바르지 않습니다.");
            }
            // 3. 오프셋 커밋
            consumer.commitSync();
        } catch (Exception e) {
            throw new RestApiException(StatusCode.KAFKA_ERROR, "Kafka 이벤트 수신 중 오류가 발생했습니다.");
        }
    }
}