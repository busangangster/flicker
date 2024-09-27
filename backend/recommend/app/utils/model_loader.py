import tensorflow as tf
from tensorflow.keras.models import Model
from tensorflow.keras.layers import Input, Dense, Dropout
from transformers import TFBertModel

SEQ_LEN = 64

def load_sentiment_model(weights_path: str) -> Model:
    # 입력 시퀀스 정의
    token_inputs = tf.keras.layers.Input((SEQ_LEN,), dtype=tf.int32, name='input_word_ids')
    mask_inputs = tf.keras.layers.Input((SEQ_LEN,), dtype=tf.int32, name='input_masks')
    segment_inputs = tf.keras.layers.Input((SEQ_LEN,), dtype=tf.int32, name='input_segment')

    # BERT 모델 로드
    bert_model = TFBertModel.from_pretrained("monologg/kobert", from_pt=True)
    bert_outputs = bert_model([token_inputs, mask_inputs, segment_inputs])[1]

    # 추가 레이어 정의
    sentiment_drop = Dropout(0.5)(bert_outputs)
    sentiment_first = Dense(1, activation='sigmoid',
                            kernel_initializer=tf.keras.initializers.TruncatedNormal(stddev=0.02))(sentiment_drop)

    # 최종 모델 정의
    sentiment_model = Model([token_inputs, mask_inputs, segment_inputs], sentiment_first)

    # 가중치 로드
    sentiment_model.load_weights(weights_path)

    return sentiment_model