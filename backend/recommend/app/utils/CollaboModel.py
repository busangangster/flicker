import sqlite3
import pandas as pd
import numpy as np
from sklearn.decomposition import PCA
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.neighbors import NearestNeighbors
import faiss
import multiprocessing as mp
import time
from tqdm import tqdm

# 전역 변수로 선언
test = None


def updatePcaResultOptimal():
    # SQLite 데이터베이스 연결
    global test
    conn = sqlite3.connect('recommend.db')

    # SQL 쿼리로 데이터를 읽어와 데이터프레임으로 변환
    query = '''SELECT * FROM review'''
    test = pd.read_sql_query(query, conn)

    # userName이 null인 행을 제거
    test = test.dropna(subset=['userName'])

    print("DB to DataFrame 추출 완")

    # 필요한 열만 선택
    test1 = test[['userName', 'movieTitle', 'grade']]

    # 피벗 테이블로 변환 (유저-영화-평점)
    pivot_test = test1.pivot_table(index='userName', columns='movieTitle', values='grade', aggfunc='first')

    pd.DataFrame(pivot_test.index).to_csv("userIndex.csv", index=False)

    print("피벗테이블 추출 완료")

    # 결측값을 0으로 채움
    pivot_test = pivot_test.fillna(0)

    # PCA 적용
    optimal_components = 5
    pca_optimal = PCA(n_components=optimal_components, svd_solver='randomized')
    pca_result_optimal = pca_optimal.fit_transform(pivot_test)

    pd.DataFrame(pca_result_optimal).to_csv("pca_result_optimal.csv", index=False)

    # 데이터베이스 연결 종료
    conn.close()


def find_nearest_neighbors_faiss(data, target_idx, n_neighbors=100):
    # DataFrame을 numpy 배열로 변환
    if isinstance(data, pd.DataFrame):
        data = data.to_numpy()

    # 데이터가 C-contiguous인지 확인하고 변환
    data = np.ascontiguousarray(data, dtype=np.float32)

    # 데이터 차원과 크기
    d = data.shape[1]

    # FAISS 인덱스 (코사인 유사도를 위한 L2 normalization 후 거리 계산)
    index = faiss.IndexFlatL2(d)

    # 인덱스에 데이터 추가
    faiss.normalize_L2(data)  # 코사인 유사도 위해 L2 정규화
    index.add(data)

    # 타겟 데이터에 대한 유사도 검색
    target_data = data[target_idx].reshape(1, -1)
    D, I = index.search(target_data, n_neighbors)

    return I[0], D[0]  # 인덱스와 거리 반환


def recommendMovieByUserTime(userName):
    start_time = time.time()

    # 1. 평점 기반 필터링 (1차 필터링)
    user_index = pd.read_csv("userIndex.csv")
    pca_result_optimal = pd.read_csv("pca_result_optimal.csv")
    print(f"데이터 로드 완료 시간: {time.time() - start_time:.4f} 초")

    # 유저 인덱스 찾기
    user_idx = user_index[user_index['userName'] == userName].index[0]
    indices, distances = find_nearest_neighbors_faiss(pca_result_optimal, user_idx, n_neighbors=100)
    print(f"유사한 사용자 검색 완료 시간: {time.time() - start_time:.4f} 초")

    # 유사한 사용자 정보 저장
    similar_users = indices[0:101]
    result = pd.DataFrame({
        'similar_user_idx': similar_users
    })
    result['userName'] = user_index.iloc[similar_users]['userName'].values

    # 1차 필터링 (유사한 유저 데이터만 사용)
    neardf = pd.merge(test, result[['userName']], on='userName', how='inner')
    print(f"1차 필터링 완료 시간: {time.time() - start_time:.4f} 초")

    # 감성 점수 기반 필터링 (2차 필터링)
    pivot_df = neardf.pivot_table(index='userName', columns='movieTitle', values='sentimentScore')
    pivot_df = pivot_df.fillna(0)

    # NearestNeighbors로 유사한 50명의 사용자 찾기
    n_neighbors = 50
    nbrs = NearestNeighbors(n_neighbors=n_neighbors, metric='cosine').fit(pivot_df)
    distances, indices = nbrs.kneighbors(pivot_df.loc[[userName]])
    print(f"2차 필터링 및 감성 점수 기반 유사 사용자 검색 완료 시간: {time.time() - start_time:.4f} 초")

    # 상위 50명의 유사한 사용자 이름 리스트 추출
    similar_users_list = pivot_df.index[indices[0]].tolist()

    # 3. "userName" 사용자가 시청한 영화 목록
    watched_movies = test.loc[test['userName'] == userName, 'movieTitle'].tolist()

    # 유사한 사용자들이 시청한 영화 중에서 사용자가 보지 않은 영화 추출 및 평점 평균 계산
    similar_users_df = pd.DataFrame(similar_users_list, columns=['userName'])
    neardf = pd.merge(test, similar_users_df[['userName']], on='userName', how='inner')

    # movieTitle을 기준으로 grade의 평균 점수 계산
    movie_grade_mean = neardf.groupby('movieTitle')['grade'].mean().reset_index()

    # 사용자가 이미 본 영화를 제외하고 추천 목록 생성
    filtered_movies = movie_grade_mean[~movie_grade_mean['movieTitle'].isin(watched_movies)]

    # 평점 기준 내림차순 정렬
    sorted_movies = filtered_movies.sort_values(by='grade', ascending=False)
    print(f"영화 평점 정렬 완료 시간: {time.time() - start_time:.4f} 초")

    # 최신 연도를 기준으로 영화 필터링
    latest_movies = test.groupby('movieTitle')['year'].idxmax().apply(lambda idx: test.loc[idx])

    # 인덱스 리셋하여 movieTitle이 중복되지 않도록 설정
    latest_movies = latest_movies.reset_index(drop=True)

    # 추천된 영화와 최신 영화를 병합
    recommended_latest_movies = pd.merge(sorted_movies, latest_movies[['movieTitle', 'year']], on='movieTitle')
    print(f"최신 영화 필터링 및 추천 목록 병합 완료 시간: {time.time() - start_time:.4f} 초")

    # 최종 결과 반환
    return recommended_latest_movies[['movieTitle', 'year']].head(30).values.tolist()