from gensim.models import Word2Vec
import re


# 저장된 모델 로드
def load_word2vec_model(model_path="app/word2vec_modelv2.model"):
    """Word2Vec 모델을 로드합니다."""
    return Word2Vec.load(model_path)


def get_similar_words_with_T(model, words_list, topn=15):
    """입력된 단어 리스트에서 유사한 단어 중 'T'로 끝나는 단어 추출"""
    similarity_dict = {}

    # 입력된 단어 리스트에서 각 단어에 대해 유사한 단어 추출
    for word in words_list:
        if word in model.wv.key_to_index:  # 단어가 모델의 vocabulary에 있는지 확인
            similar_words = model.wv.most_similar(word, topn=500)  # 충분히 많은 단어를 가져옴
            for similar_word, similarity in similar_words:
                # 'T'로 끝나는 단어만 처리
                if similar_word.endswith('T'):
                    if similar_word in similarity_dict:
                        similarity_dict[similar_word] += similarity  # 유사도 합산
                    else:
                        similarity_dict[similar_word] = similarity
        else:
            # 단어가 모델에 없으면 패스
            print(f"'{word}' is not in the vocabulary. Skipping.")

    # 유사도에 따라 정렬
    sorted_similar_words = sorted(similarity_dict.items(), key=lambda x: x[1], reverse=True)

    filtered_words = [word for word, _ in sorted_similar_words[:topn] if word not in words_list]

    # 상위 topn개의 단어만 반환
    return split_title_and_year(filtered_words)


# 영화 제목과 연도를 나누는 함수
def split_title_and_year(movies):
    split_movies = []
    for movie in movies:
        # 'T' 제거 및 '^'로 영화 제목과 연도 분리
        movie = movie[:-1]
        title, year = re.split(r'\^', movie)  # '^'로 분리
        split_movies.append((title, int(year)))  # 튜플로 저장

    # 연도 기준 내림차순 정렬
    split_movies_sorted = sorted(split_movies, key=lambda x: x[1], reverse=True)

    return split_movies_sorted