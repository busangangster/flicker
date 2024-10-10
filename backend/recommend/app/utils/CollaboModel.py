import pandas as pd
import numpy as np
from sklearn.decomposition import PCA
import faiss
import sqlite3
import asyncio

# 글로벌 변수로 PCA 결과와 test 데이터 저장
pca_cache = None
test = None


def load_pca_from_cache():
    """캐시에 있는 PCA 결과를 반환"""
    global pca_cache
    if pca_cache is not None:
        return pca_cache
    else:
        # 캐시가 비어있으면 기본 PCA 결과를 로드 (초기 로딩)
        return pd.read_csv("pca_result_optimal.csv")


async def updatePcaResultOptimal():
    global test, pca_cache
    # SQLite 데이터베이스 연결
    conn = sqlite3.connect('recommend.db')

    # SQL 쿼리로 데이터를 읽어와 데이터프레임으로 변환
    query = '''SELECT * FROM review_info'''
    new_test = pd.read_sql_query(query, conn)

    print("db 끝")

    # user_seq가 null인 행을 제거
    new_test = new_test.dropna(subset=['user_seq'])

    # 피벗 테이블로 변환 (유저-영화-평점)
    pivot_test = new_test.pivot_table(index='user_seq', columns='movie_seq', values='review_rating', aggfunc='first')

    print("pivot 끝")

    # 결측값을 0으로 채움
    pivot_test = pivot_test.fillna(0)

    # PCA 적용
    optimal_components = 5
    pca_optimal = PCA(n_components=optimal_components, svd_solver='randomized')
    pca_result_optimal = pca_optimal.fit_transform(pivot_test)

    print("pca 끝")

    # PCA 결과 저장
    pca_result_optimal = pd.DataFrame(pca_result_optimal)
    pca_result_optimal['user_seq'] = pivot_test.index
    pd.DataFrame(pca_result_optimal).to_csv("pca_result_optimal.csv", index=False)

    print("pca 저장")

    # 캐시에 새로운 PCA 결과 저장
    pca_cache = pca_result_optimal
    test = new_test

    print("update success")

    # 데이터베이스 연결 종료
    conn.close()


def find_nearest_neighbors_faiss(data, target_idx, n_neighbors=100):
    if isinstance(data, pd.DataFrame):
        data = data.to_numpy()

    data = np.ascontiguousarray(data, dtype=np.float32)

    d = data.shape[1]

    index = faiss.IndexFlatL2(d)
    faiss.normalize_L2(data)  # 코사인 유사도 위해 L2 정규화
    index.add(data)

    target_data = data[target_idx].reshape(1, -1)
    D, I = index.search(target_data, n_neighbors)

    return I[0], D[0]  # 인덱스와 거리 반환


def recommendMovieByUserTime(userSeq):
    global pca_cache, test
    if test is None:
        raise Exception("Test data not loaded. Please load the data first.")

    pca_result_optimal = load_pca_from_cache()
    user_idx = pca_result_optimal[pca_result_optimal['user_seq'] == userSeq].index[0]

    indices, distances = find_nearest_neighbors_faiss(pca_result_optimal, user_idx, n_neighbors=100)

    nUserSeq = pca_result_optimal.loc[indices, 'user_seq'].values
    kUserDf = test[test['user_seq'].isin(nUserSeq)]

    # 감성 점수 기반 필터링 (2차 필터링)
    pivot_df = kUserDf.pivot_table(index='user_seq', columns='movie_seq', values='sentiment_score')
    pivot_df = pivot_df.fillna(0)

    optimal_components = 5
    pca_optimal = PCA(n_components=optimal_components, svd_solver='randomized')
    pca_result_optimal2 = pca_optimal.fit_transform(pivot_df)

    pca_result_optimal2 = pd.DataFrame(pca_result_optimal2)
    pca_result_optimal2['user_seq'] = pivot_df.index
    user_idx = pca_result_optimal2[pca_result_optimal2['user_seq'] == userSeq].index[0]

    indices, distances = find_nearest_neighbors_faiss(pca_result_optimal2, user_idx, n_neighbors=51)

    kuserList = pca_result_optimal2.loc[indices[1:], 'user_seq'].values
    watched_movie = list(set(test[test['user_seq'] == userSeq]['movie_seq']))
    filtered_df = test[test['user_seq'].isin(kuserList) & ~test['movie_seq'].isin(watched_movie)]

    result_df = filtered_df.groupby('movie_seq')['sentiment_score'].mean().reset_index()

    # 평균값을 기준으로 내림차순 정렬
    result_df = result_df.sort_values(by='sentiment_score', ascending=False)

    # 결과 출력
    return searchMovieTitleByMovieSeq(result_df['movie_seq'][:30].values)


def searchMovieTitleByMovieSeq(movie_list):
    conn = sqlite3.connect('recommend.db')
    cursor = conn.cursor()

    result_list = []

    for movie_seq in movie_list:
        cursor.execute("SELECT movie_title, movie_year FROM movie_info WHERE movie_seq = ?", (int(movie_seq),))

        result = cursor.fetchone()

        if result:
            result_list.append(list(result))

    return sorted(result_list, key=lambda x: x[1], reverse=True)


async def load_initial_data():
    """애플리케이션 시작 시 초기 PCA 결과와 test 데이터 로드"""
    global pca_cache, test
    try:
        # 기존 PCA 결과를 캐시에 로드
        pca_cache = pd.read_csv("pca_result_optimal.csv")
        print("PCA result loaded into cache.")

        # SQLite 데이터베이스 연결
        conn = sqlite3.connect('recommend.db')

        # SQL 쿼리로 데이터를 읽어와 test 변수에 저장
        query = '''SELECT * FROM review_info'''
        test = pd.read_sql_query(query, conn)
        print("Test data loaded.")

        # 데이터베이스 연결 종료
        conn.close()

    except Exception as e:
        print(f"Error loading data: {e}")