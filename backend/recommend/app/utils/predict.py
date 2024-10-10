import numpy as np
from transformers import AutoTokenizer
from model import SEQ_LEN, load_model
from tqdm import tqdm

# 토크나이저 로드
tokenizer = AutoTokenizer.from_pretrained('monologg/kobert', trust_remote_code=True)

# 모델 로드
sentiment_model = load_model()


def sentence_convert_data(data, tokenizer, seq_len):
    """문장을 BERT 모델 입력 형태로 변환합니다."""
    tokens, masks, segments = [], [], []

    token = tokenizer.encode(data, max_length=seq_len, truncation=True, padding='max_length')
    num_zeros = token.count(0)
    mask = [1] * (seq_len - num_zeros) + [0] * num_zeros
    segment = [0] * seq_len

    tokens.append(token)
    segments.append(segment)
    masks.append(mask)

    return [np.array(tokens), np.array(masks), np.array(segments)]


def movie_evaluation_predict(sentences):
    """배치 감정 분석 예측을 수행합니다."""
    tokens_batch, masks_batch, segments_batch = [], [], []

    for sentence in sentences:
        data_x = sentence_convert_data(sentence, tokenizer, SEQ_LEN)
        tokens_batch.append(data_x[0][0])
        masks_batch.append(data_x[1][0])
        segments_batch.append(data_x[2][0])

    tokens_batch = np.array(tokens_batch)
    masks_batch = np.array(masks_batch)
    segments_batch = np.array(segments_batch)

    predictions = sentiment_model.predict([tokens_batch, masks_batch, segments_batch])
    predict_values = np.ravel(predictions)
    return [round(predict_value.item(), 2) for predict_value in predict_values]


def sequential_movie_evaluation(reviews, batch_size=32):
    results = []
    for i in tqdm(range(0, len(reviews), batch_size), desc="Predicting sentiment scores", ncols=100):
        batch_reviews = reviews[i:i + batch_size]
        batch_sentences = [review.content for review in batch_reviews]
        batch_results = movie_evaluation_predict(batch_sentences)
        for review, score in zip(batch_reviews, batch_results):
            results.append({
                "reviewSeq": review.reviewSeq,
                "sentimentScore": score
            })
    return results
