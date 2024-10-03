from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from bs4 import BeautifulSoup
import sys
import io
import time
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from models import Movie, Actor
import re
from selenium.webdriver.common.by import By  # By 모듈 임포트
import base64

# 표준 출력의 인코딩을 UTF-8로 설정하여 출력 시 한글이 깨지지 않도록 설정
sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding='utf-8')

movieSeqList = []

def initialize_driver():
    """
    Selenium ChromeDriver를 초기화하고 반환하는 함수.
    """
    try:
        chrome_options = Options()
        chrome_options.add_argument("--headless")  # 브라우저 창을 열지 않음
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--disable-dev-shm-usage")
        chrome_options.add_argument("--disable-gpu")
        chrome_options.add_argument("--disable-infobars")
        chrome_options.add_argument("--disable-extensions")

        # User-Agent 설정
        user_agent = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36'
        chrome_options.add_argument(f'user-agent={user_agent}')
        
        # Chrome WebDriver 초기화
        driver = webdriver.Chrome(options=chrome_options)
        return driver

    except Exception as e:
        print(f"Error initializing driver: {e}")
        sys.exit(1)


def collect_movie_titles(driver):
    """
    네이버 검색 페이지에서 영화 제목과 개봉 연도를 수집하여 리스트로 반환하는 함수.
    
    Parameters:
    - driver (webdriver.Chrome): Selenium WebDriver 인스턴스

    Returns:
    - movie_info_list (list): 수집된 영화 제목 및 개봉 연도 리스트 (예: [{'title': '영화제목', 'year': '2024'}])
    """
    # 네이버 검색 URL
    url = "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query=%EC%9D%B4%EB%B2%88%EB%8B%AC+%EA%B0%9C%EB%B4%89%EC%98%81%ED%99%94&oquery=%EC%9D%B4%EB%B2%88%EC%A3%BC+%EA%B0%9C%EB%B4%89%EC%98%81%ED%99%94&tqi=iYGVgspzL8wssdL3Py0ssssssQo-358768"

    # 영화 제목과 연도를 담을 리스트
    movie_info_list = []

    try:
        # 크롤링할 URL로 이동
        driver.get(url)
        time.sleep(3)  # 페이지가 로드될 때까지 대기 (필요에 따라 조정)

        # '다음' 버튼 클릭을 반복하며 데이터를 수집
        while True:
            # 페이지 소스를 BeautifulSoup으로 파싱
            soup = BeautifulSoup(driver.page_source, "html.parser")

            # 'card_area _panel' 클래스를 가진 div 요소를 찾음
            card_panel = soup.find("div", class_="card_area _panel")

            # card_panel 내에서 'title multi_line _ellipsis' 클래스 div 요소들을 모두 찾음
            if card_panel:
                title_divs = card_panel.find_all("div", class_="title multi_line _ellipsis")

                # 각 'title multi_line _ellipsis' 요소에서 'a' 태그 텍스트를 추출하여 리스트에 추가
                for title_div in title_divs:
                    a_tag = title_div.find("a")  # 각 title_div 안의 a 태그 찾기
                    if a_tag:
                        movie_title = a_tag.get_text().strip()  # 영화 제목 추출 및 공백 제거
                        
                        # 영화 제목의 부모 요소에서 추가 정보를 탐색
                        parent_div = title_div.find_parent("div", class_="card_area _panel")
                        if parent_div:
                            # 개봉 연도 및 평점 정보를 담고 있는 <dl> 태그 찾기
                            info_group = parent_div.find("dl", class_="info_group type_visible")
                            movie_year = None  # 개봉 연도 초기화
                            if info_group:
                                # <dl> 태그 내의 <dt>와 <dd> 요소를 모두 추출
                                dd_tags = info_group.find("dd")
                                if dd_tags:
                                    movie_year = dd_tags.get_text().split('.')[0].strip()

                            # 영화 제목과 개봉 연도를 함께 저장
                            movie_info_list.append({'title': movie_title, 'year': movie_year})

            # '다음' 버튼을 찾기
            try:
                next_button = driver.find_element(By.CSS_SELECTOR, 'a.pg_next._next.on')

                # 'aria-disabled' 속성이 "true"일 경우 더 이상 클릭하지 않음
                if next_button.get_attribute("aria-disabled") == "true":
                    break

                # '다음' 버튼이 활성화된 경우 클릭
                next_button.click()
                time.sleep(2)  # 페이지가 로드될 시간을 줌

            except Exception as e:
                break

    except Exception as e:
        print(f"Error during crawling: {e}")

    return movie_info_list


def search_and_find_movie(driver, movie_info_list):
    """
    영화 목록 CSV 파일을 읽어서 각 영화에 대한 정보를 추출하고, 
    성공적으로 처리된 영화의 행은 파일에서 삭제합니다.

    Parameters:
    year (str): 연도
    """
    remaining_rows = []  # 성공적으로 처리되지 않은 행을 저장할 리스트
    for movie_info in movie_info_list:
        try:
            movie_title = movie_info.get('title')  # 영화 제목 추출
            movie_year = movie_info.get('year')    # 영화 연도 추출
            # 영화 데이터를 가져오는 함수 호출
            if(fetch_movie_data(driver, str(movie_title), movie_year) == False):
                remaining_rows.append(movie_info)  # 실패한 행은 유지
        except KeyError:
            remaining_rows.append(movie_info)  # 실패한 행은 유지
        except Exception as e:
            remaining_rows.append(movie_info)  # 실패한 행은 유지

    if len(remaining_rows) > 0:
        return remaining_rows
    else:
        return []
    
# 영화 찾기 메서드 
def fetch_movie_data(driver, movie_title, year):
    """
    영화 제목을 받아 해당 영화를 검색하고, 영화 정보와 리뷰를 추출하여 CSV 파일에 저장하는 함수.
    
    Parameters:
    movie_title (str): 영화 제목
    year (str): 연도
    """
    try:
        # 영화 제목을 포함한 검색 결과 페이지 URL 생성
        search_url = f"https://pedia.watcha.com/ko-KR/search?query={movie_title}"
        # 검색 결과 페이지로 이동
        driver.get(search_url)
        driver.implicitly_wait(10)
        time.sleep(1)

        # 페이지 소스를 가져와서 BeautifulSoup 객체로 파싱
        html = driver.page_source
        soup = BeautifulSoup(html, "html.parser")
        li_elements = None

        try:
            # 검색 결과 섹션을 찾음
            section = soup.find("section", class_="e3425a05cc8981969106 d34b60d5a027fe9be462")
            
            # 섹션 내 모든 영화 리스트 아이템을 찾음
            if section:
                li_elements = section.find_all("li", class_="c1fbb66dd0a8919a619b ef9348f4092a69aeb892")
            else:
                div_element = soup.find("div", class_="d54b5ff7a8eaf4d71c6d")
                # 검색 결과 없음
                if div_element:
                    return True
                # 요청 거부
                return False
        except AttributeError:
            # 섹션 또는 영화 리스트를 찾지 못했을 경우 오류 메시지 출력 후 함수 종료
            print(f"Error: Unable to find the section or movie list for {movie_title}")
            return False
        
        # 각 영화 리스트 아이템을 순회하면서 처리
        if li_elements:
            for li in li_elements:
                try:
                    # 각 영화 리스트 아이템의 링크 태그를 찾음
                    a_tag = li.find("a", class_="InnerPartOfListWithImage e243b4e8439813c3ed15 ead347fbee413c8c2a9b")
                    
                    # 해당 링크 태그의 제목이 우리가 찾고자 하는 영화 제목과 일치하는지 확인
                    if a_tag and a_tag['title'].replace(" ", "").strip() == movie_title.replace(" ", "").strip():
                        # 영화 상세 페이지 URL 생성
                        movie_url = "https://pedia.watcha.com" + a_tag['href']
                        
                        # 영화 상세 페이지로 이동
                        driver.get(movie_url)
                        
                        # 페이지가 완전히 로드될 때까지 대기
                        driver.implicitly_wait(10)
                        time.sleep(1)

                        # 영화 상세 페이지의 HTML을 가져와서 BeautifulSoup 객체로 파싱
                        movie_html = driver.page_source
                        movie_soup = BeautifulSoup(movie_html, "html.parser")

                        # 영화 정보 섹션에서 연도, 장르, 나라 정보 추출
                        info_elements = movie_soup.find_all("div", class_="acb1a192f548da4feda1 cbf385a8dfd716be802c")
                        if info_elements:
                            # 개봉년도, 장르, 나라 추출
                            movieYear = None
                            movieYear = None
                            genre = None
                            country = None
                            if len(info_elements) > 1:
                                year_genre_country = info_elements[1].text.strip().split('·')
                                if len(year_genre_country) > 2:
                                    movieYear = year_genre_country[0].strip()
                                    genre = year_genre_country[1].strip()
                                    country = year_genre_country[2].strip()
                            # 러닝타임과 관람등급 정보 추출
                            running_time = None
                            audience = None
                            if len(info_elements) > 2:
                                running_time_audience = info_elements[2].text.strip().split('·')
                                if len(running_time_audience) > 1:
                                    running_time = running_time_audience[0].strip()
                                    audience = running_time_audience[1].strip()

                            # 영화 일치 여부 확인 ( 연도로 확인 )
                            if movieYear == year:
                                # 추가 데이터 추출 후 CSV 파일로 저장
                                write_movie_data(movie_soup, movie_title, genre, country, running_time, audience, year)
                                # 매칭된 영화 찾음
                                return True
                except Exception as e:
                    print(f"Error processing movie: {e}", movie_title)
                    continue
            # 영화를 찾지는 못했으나 에러는 아님    
            return True
    except Exception as e:
        print(f"Error fetching movie data: {e}", movie_title)
        return False
    

# 영화 상세정보 저장 메서드
def write_movie_data(movie_soup, movie_title, genre, country, running_time, audience, year):
    """
    영화 정보를 CSV 파일에 기록하는 함수.
    
    Parameters:
    movie_soup (BeautifulSoup): 영화 상세 페이지의 BeautifulSoup 객체
    movie_title (str): 영화 제목
    genre (str): 영화 장르
    country (str): 제작 국가
    running_time (str): 상영 시간
    audience (str): 관람 등급
    year (str): 개봉 연도
    """
    try:
            # 영화의 감독 및 출연자 정보 섹션 찾기
            cast_info_section = movie_soup.find("div", class_="cc6c6c81887bf112cdd4 listWrapper")
            cast_info = []  # 출연자 정보를 담을 리스트
            director_list = [] # 감독 정보를 담을 리스트
            # 감독 , 출연진 정보 추출
            if cast_info_section:
                cast_divs = cast_info_section.find_all("div", class_="ec0b9ed1dbb86d0b6184")
                if cast_divs:
                    for cast_div in cast_divs:
                        name = None
                        role = None
                        name_element = cast_div.find("div", class_="dd54bbb3c2b69c54c8fc ec12a50985dc3679b6b1")
                        if name_element:
                            name = name_element.text.strip()
                        role_element = cast_div.find("div", class_="e7b998e3c9d63eb5921a cbf385a8dfd716be802c b422b149fa6c740a8a58")
                        if role_element:
                            role = role_element.text.strip()
                        if role == "감독":
                            director_list.append(name)
                        else:
                            cast_info.append(f"{name} ({role})")

            # 줄거리 추출
            plot_element = movie_soup.find("p", class_="cbf385a8dfd716be802c")
            plot = None
            if plot_element:
                plot = plot_element.text.strip()

            # 포스터 이미지 URL 추출
            image_element = movie_soup.find("div", class_="e986acb87595fabe84dc a687e116ed254fa2f1fd dc9c3d0a66c0affbade8")
            image_url = None
            if image_element:
                image_url = image_element.find("img")["src"]

            # 백그라운드 이미지 URL 추출
            background_image_element = movie_soup.find("div", class_="d5d376293ff34390f11f")
            background_image_url = None
            if background_image_element:
                style_attribute = background_image_element.get("style")
                background_image_url = style_attribute[21:-1]

            # 예고편 URL 추출
            trailer_url = None
            ul_tag = movie_soup.find("ul", class_="d66339e2b69adf69b22f e47cb6a241fb8eb3c810")
            if ul_tag:
                li_elements = ul_tag.find_all("li", class_="c5c4891385919a827406 db96a9bd17142a5de66d")
                for li in li_elements:
                    a_tag = li.find("a", class_="dee9cb333a0540526ecb")
                    if a_tag:
                        trailer_url = a_tag["href"]
                        break

            # DB저장
            save_movie_data_to_db(movie_title, plot, year, genre, country, image_url, background_image_url, director_list, cast_info, running_time, audience, trailer_url)

    except Exception as e:
        # 오류 발생 시 메시지 출력
        print(f"Error writing movie data: {e}: ", movie_title)
   

def save_movie_data_to_db(movie_title, plot, year, genre, country, image_url, background_image_url, director_list, cast_info, running_time, audience, trailer_url):
    """
    영화 정보를 MySQL 데이터베이스에 저장하는 함수. 이미 movie_title과 year가 동일한 영화가 있으면 저장하지 않음.
    
    Parameters:
    - movie_title (str): 영화 제목
    - plot (str): 줄거리
    - year (int): 개봉 연도
    - genre (str): 장르
    - country (str): 제작 국가
    - image_url (str): 포스터 이미지 URL
    - background_image_url (str): 백그라운드 이미지 URL
    - director_list (list): 감독 리스트
    - cast_info (list): 출연자 정보 리스트
    - running_time (str): 상영 시간
    - audience (str): 관람 등급
    - trailer_url (str): 예고편 URL
    """
    try:
        # 세션 생성
        session = create_session()

        # 기존 영화가 존재하는지 확인 (movie_title과 year를 기준으로 조회)
        existing_movie = session.query(Movie).filter_by(movie_title=movie_title, movie_year=year).first()

        if existing_movie:
            movieSeqList.append(existing_movie.movie_seq)
            return  # 함수 종료

        # Movie 객체 생성 및 세션에 추가
        movie = Movie(
            movie_title=movie_title,
            director=", ".join(director_list) if director_list else None,
            genre=genre,
            country=country,
            movie_plot=plot,
            audience_rating=audience,
            movie_year=year,
            running_time=running_time,
            movie_rating=0.0,  # 평점 필드가 없는 경우 기본값 0.0
            movie_poster_url=image_url,
            trailer_url=decode_trailer_url(trailer_url),
            background_url=background_image_url,
            del_yn='N'  # 삭제 여부를 기본값 'N'으로 설정
        )
        session.add(movie)
        session.flush()  # movie_seq를 얻기 위해 flush() 수행

        # 배우 정보 문자열 파싱 및 처리
        for actor_info in cast_info:
            # 괄호의 짝을 맞추고 괄호 밖의 쉼표로 분리
            actor_list = []
            buffer = ''
            open_paren = 0  # 괄호가 열려 있는지 확인하는 변수

            for char in actor_info:
                if char == '(':
                    open_paren += 1
                    buffer += char
                elif char == ')':
                    open_paren -= 1
                    buffer += char
                elif char == ',' and open_paren == 0:
                    # 괄호가 닫혀있을 때만 쉼표로 구분
                    actor_list.append(buffer.strip())
                    buffer = ''
                else:
                    buffer += char

            # 마지막에 남은 buffer 추가
            if buffer:
                actor_list.append(buffer.strip())

            # 각 배우 정보를 데이터베이스에 저장
            for actor_detail in actor_list:
                # 배우 이름과 역할을 정규식을 사용하여 추출
                match = re.match(r"(.+?)\s*\((.+)\)", actor_detail)
                if match:
                    actor_name = match.group(1).strip()  # 배우 이름
                    role = match.group(2).strip()  # 괄호 안의 내용
                else:
                    # 괄호가 없으면 역할 정보가 없는 경우
                    actor_name = actor_detail.strip()
                    role = None

                # Actor 객체 생성 및 저장
                actor = Actor(
                    actor_name=actor_name,
                    role=role,
                    movie_seq=movie.movie_seq  # 생성된 movie_seq 사용
                )
                session.add(actor)
        
        # 최종적으로 한 번의 커밋으로 영화 및 배우 정보 저장
        session.commit()
        movieSeqList.append(movie.movie_seq)
    
    except Exception as e:
        print(f"Error saving movie data to database: {e}")
        session.rollback()

def decode_trailer_url(encoded_url):
    """
    URL-safe base64로 인코딩된 문자열을 디코딩하여 실제 유튜브 링크로 변환하는 함수.
    디코딩이 실패하면 'InvalidURL'로 처리.
    """
    if encoded_url == None:
        return encoded_url  # "N/A" 값이거나 null 값일 경우 그대로 반환
    
    try:
        # 리디렉션된 URL에서 base64 인코딩된 부분만 추출
        encoded_part = encoded_url.split('/')[-1]

        # URL-safe base64 처리: '-' -> '+', '_' -> '/'
        encoded_part = encoded_part.replace('-', '+').replace('_', '/')

        # base64 문자열의 길이가 4의 배수가 되도록 '=' 패딩 추가
        missing_padding = len(encoded_part) % 4
        if missing_padding != 0:
            encoded_part += '=' * (4 - missing_padding)

        # base64 디코딩 시도
        decoded_url = base64.b64decode(encoded_part).decode('utf-8')

        embed_url = decoded_url.replace("watch?v=", "embed/")

        # 유튜브 링크를 반환
        return embed_url
    except Exception as e:
        # 디코딩 실패 시 에러 로그와 encoded_part 출력
        print(f"Error decoding trailer URL '{encoded_url}': {e}")
        print(f"Encoded part: {encoded_part}")
        return None



def create_session():
    """
    SQLAlchemy 세션을 생성하고 반환하는 함수.
    """
    user = 'root'
    password = '1234'
    host = '3.36.106.130'   
    port = '32263'        
    database = 'flicker'

    movieEngine = create_engine(f"mysql+pymysql://{user}:{password}@{host}:{port}/{database}")

    # 세션 생성기
    Session = sessionmaker(bind=movieEngine)
    session = Session()

    return session  # 생성된 세션 객체 반환



def main_process():
    # Selenium ChromeDriver 초기화
    driver = initialize_driver()

    # 이번달 개봉 영화 정보 수집
    movie_info_list = collect_movie_titles(driver)

    # 수집된 영화 제목들 상세 정보 크롤링 및 DB 저장
    while True:
        movie_info_list = search_and_find_movie(driver, movie_info_list)
        if(len(movie_info_list) == 0):
            break
    
    # 드라이버 종료
    driver.quit()

    print(movieSeqList)


# 프로그램 실행
if __name__ == "__main__":
    main_process()
