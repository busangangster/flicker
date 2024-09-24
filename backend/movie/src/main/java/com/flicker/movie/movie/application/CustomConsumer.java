package com.flicker.movie.movie.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.movie.common.module.exception.RestApiException;
import com.flicker.movie.common.module.status.StatusCode;
import com.flicker.movie.movie.config.KafkaConfig;
import com.flicker.movie.movie.domain.entity.Movie;
import com.flicker.movie.movie.domain.entity.WordCloud;
import com.flicker.movie.movie.dto.KeywordCount;
import com.flicker.movie.movie.dto.MovieRatingEvent;
import com.flicker.movie.movie.dto.WordCloudEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomConsumer {

    private final KafkaConfig config;
    private final MovieBuilderUtil movieBuilderUtil;
    private KafkaConsumer<String, String> consumer = null;
    private final ObjectMapper objectMapper;
    private final MovieRepoUtil movieRepoUtil;

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
        } catch (Exception e) {
            throw new RestApiException(StatusCode.KAFKA_ERROR, "Kafka 이벤트 수신 중 오류가 발생했습니다.");
        }
    }

    // Kafka 메시지 수진 ( 영화 워드 클라우드 업데이트 )
    @KafkaListener(topics = "${spring.kafka.template.wordcloud-result-topic}")
    @Transactional
    public void consumeWordCloud(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String payload) {
        try {
            // 1. 역직렬화: payload를 WordCloudEvent 객체로 변환
            WordCloudEvent wordCloudEvent = objectMapper.readValue(payload, WordCloudEvent.class);
            // 2. 영화 ID와 키워드 추출
            int movieSeq = wordCloudEvent.getMovieSeq();
            List<KeywordCount> keywordCounts = wordCloudEvent.getKeywordCounts();
            // 3. DB에서 해당 영화 조회
            Movie movie = movieRepoUtil.findById(movieSeq);
            // 4. 기존 워드 클라우드 초기화
            movie.clearWordClouds();
            // 5. 영화 워드 클라우드 추가
            List<WordCloud> wordClouds = movieBuilderUtil.buildWordCloudList(keywordCounts);
            movie.addWordClouds(wordClouds);
        } catch (Exception e) {
            throw new RestApiException(StatusCode.KAFKA_ERROR, "Kafka 이벤트 수신 중 오류가 발생했습니다.");
        }
    }

}