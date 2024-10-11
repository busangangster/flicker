import sqlite3
import pandas as pd
import re
from konlpy.tag import Okt
from collections import Counter

okt = Okt()


# Pydantic 모델 정의
class WordCloud:
    def __init__(self, keyword, count):
        self.keyword = keyword
        self.count = count


class WordCloudResult:
    def __init__(self, movieSeq, keywordCounts):
        self.movieSeq = movieSeq
        self.keywordCounts = keywordCounts


# 특수 문자 제거 함수
def clean_text(text):
    """한글만 남기고 나머지 문자는 제거"""
    return re.sub(r'[^\uAC00-\uD7A3\s]+', '', text)


# 명사 추출 및 빈도수 계산 함수
def extract_nouns_and_calculate_freqs(grouped_df):
    """각 movie_seq에 대해 명사를 추출하고 빈도수를 계산"""
    results = []
    for idx, row in grouped_df.iterrows():
        movie_seq = row['movie_seq']
        content = row['content']

        # 명사 추출
        nouns = okt.nouns(content)

        # 한 글자인 명사를 제외하고 필터링
        filtered_nouns = [noun for noun in nouns if len(noun) > 1]

        # 명사 빈도수 계산
        noun_freqs = Counter(filtered_nouns)

        # 각 단어와 빈도수를 movie_seq와 함께 저장
        keyword_counts = [WordCloud(keyword=word, count=freq) for word, freq in noun_freqs.items()]

        # WordCloudResult 모델을 사용해 결과 저장
        result = WordCloudResult(
            movieSeq=movie_seq,
            keywordCounts=keyword_counts
        )
        results.append(result)

    return results


# SQLite에서 데이터를 가져오는 함수
def fetch_wordcloud_data():
    """SQLite 데이터베이스에서 wordcloud 테이블의 데이터를 가져옴"""
    conn = sqlite3.connect('recommend.db')
    query = "SELECT movie_seq, content FROM wordcloud limit 100"
    df = pd.read_sql_query(query, conn)
    conn.close()

    return df


def wordcloudUpdate():
    # SQLite에서 데이터 로드
    df = fetch_wordcloud_data().dropna()
    grouped = df.groupby('movie_seq')['content'].apply(lambda x: clean_text(' '.join(x))).reset_index()
    # grouped = df.groupby('movie_seq')['content'].apply(lambda x: ' '.join(set(x))).reset_index()

    print(grouped)

    # 명사 추출 및 빈도수 계산
    results = extract_nouns_and_calculate_freqs(grouped)

    # 결과 출력
    # for result in results:
    #     print(f"MovieSeq: {result.movieSeq}")
    #     for keyword in result.keywordCounts:
    #         print(f"Keyword: {keyword.keyword}, Count: {keyword.count}")
    return results
