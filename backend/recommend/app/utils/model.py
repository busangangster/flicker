from tensorflow.keras.models import Model
from tensorflow.keras.layers import Input, Dense, Dropout
from transformers import TFBertModel
import tensorflow as tf

SEQ_LEN = 64
TOKENIZER_NAME = 'monologg/kobert'
MODEL_PATH = 'app/sentiment_model_weights.h5'

def create_sentiment_model(seq_len):
    """감정 분석 모델을 생성합니다."""
    token_inputs = Input((seq_len,), dtype=tf.int32, name='input_word_ids')
    mask_inputs = Input((seq_len,), dtype=tf.int32, name='input_masks')
    segment_inputs = Input((seq_len,), dtype=tf.int32, name='input_segment')

    # BERT 모델 로드
    bert_model = TFBertModel.from_pretrained(TOKENIZER_NAME, from_pt=True)
    bert_outputs = bert_model([token_inputs, mask_inputs, segment_inputs])[1]

    # 추가 레이어 정의
    sentiment_drop = Dropout(0.5)(bert_outputs)
    sentiment_first = Dense(1, activation='sigmoid', kernel_initializer=tf.keras.initializers.TruncatedNormal(stddev=0.02))(sentiment_drop)

    # 최종 모델 정의
    return Model([token_inputs, mask_inputs, segment_inputs], sentiment_first)

def load_model():
    model = create_sentiment_model(SEQ_LEN)
    model.load_weights(MODEL_PATH)
    return model