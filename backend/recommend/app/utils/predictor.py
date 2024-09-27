import numpy as np
from transformers import AutoTokenizer
from tensorflow.keras.models import Model

SEQ_LEN = 64

# 토크나이저 로드
tokenizer = AutoTokenizer.from_pretrained('monologg/kobert', trust_remote_code=True)

def sentence_convert_data(data: str):
    tokens, masks, segments = [], [], []
    token = tokenizer.encode(data, max_length=SEQ_LEN, truncation=True, padding='max_length')

    num_zeros = token.count(0)
    mask = [1]*(SEQ_LEN-num_zeros) + [0]*num_zeros
    segment = [0]*SEQ_LEN

    tokens.append(token)
    segments.append(segment)
    masks.append(mask)

    tokens = np.array(tokens)
    masks = np.array(masks)
    segments = np.array(segments)
    return [tokens, masks, segments]

def movie_evaluation_predict(model: Model, sentence: str) -> float:
    data_x = sentence_convert_data(sentence)
    predict = model.predict(data_x)
    predict_value = np.ravel(predict)
    return round(predict_value.item(), 2)