import matplotlib.pyplot as plt
import pandas as pd
from tqdm import tqdm_notebook
import matplotlib.pyplot as plt
import urllib.request
from gensim.models.word2vec import Word2Vec
from gensim.models.callbacks import CallbackAny2Vec
from tqdm import tqdm


# Word2Vec 진행 상황을 추적하면서 손실값을 저장할 수 있도록 Callback 클래스 수정
class LossLogger(CallbackAny2Vec):
    def __init__(self, total_epochs):
        self.epoch = 0
        self.losses = []
        self.total_epochs = total_epochs
        self.pbar = tqdm(total=total_epochs, desc="Word2Vec Training", unit="epoch")

    def on_epoch_end(self, model):
        # 각 에포크가 끝날 때 손실값을 가져와 기록
        loss = model.get_latest_training_loss()
        if self.epoch == 0:
            self.losses.append(loss)  # 첫 번째 에포크에서는 그대로 기록
        else:
            self.losses.append(loss - self.losses[-1])  # 이전 손실값과의 차이를 기록
        self.epoch += 1
        self.pbar.update(1)

    def on_train_end(self, model):
        self.pbar.close()


async def model_update():
    total_epochs = 250  # 학습할 epoch 수 설정
    loss_logger = LossLogger(total_epochs)

    # Word2Vec 모델 학습
    model = Word2Vec(
        sentences=data['tokens'],
        vector_size=300,
        window=5,
        min_count=2,
        workers=4,
        epochs=total_epochs,
        compute_loss=True,
        callbacks=[loss_logger]  # 진행 상황을 추적하는 callback 추가
    )

    # Word2Vec 모델을 파일로 저장
    model.save("app/word2vec_modelv2.model")