from fastapi import FastAPI, HTTPException
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from bs4 import BeautifulSoup
import sys
import io
import time
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from models import Movie as DbMovie, Actor as DbActor  # SQLAlchemy 모델 임포트
import re
from selenium.webdriver.common.by import By
import base64
from typing import List, Optional
from pydantic import BaseModel

# FastAPI 인스턴스 생성
app = FastAPI()

# 표준 출력의 인코딩을 UTF-8로 설정하여 출력 시 한글이 깨지지 않도록 설정
sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding='utf-8')

# 전역 변수로 response 정의
response = []

# Pydantic 모델 정의 (SQLAlchemy와 충돌 방지 위해 이름 변경)
class ActorResponse(BaseModel):
    actor_name: str
    # role: Optional[str] = None

class MovieResponse(BaseModel):
    movie_seq: int
    movie_title: str
    # director: Optional[str]
    genre: Optional[str]
    # country: Optional[str]
    # movie_plot: Optional[str]
    # audience_rating: Optional[str]
    movie_year: int
    # running_time: Optional[str]
    # movie_rating: Optional[float]
    # movie_poster_url: Optional[str]
    # trailer_url: Optional[str]
    # background_url: Optional[str]
    del_yn: str
    actors: Optional[List[ActorResponse]] = None

def convert_movie_to_response(movie, actors=None):
    """
    SQLAlchemy Movie 객체를 Pydantic MovieResponse 모델로 변환하는 함수.
    """
    if actors:
        actors_response = [ActorResponse(actor_name=actor.actor_name) for actor in actors]
    else:
        actors_response = None

    return MovieResponse(
        movie_seq=movie.movie_seq,
        movie_title=movie.movie_title,
        # director=movie.director,
        genre=movie.genre,
        # country=movie.country,
        # movie_plot=movie.movie_plot,
        # audience_rating=movie.audience_rating,
        movie_year=movie.movie_year,
        # running_time=movie.running_time,
        # movie_rating=movie.movie_rating,
        # movie_poster_url=movie.movie_poster_url,
        # trailer_url=movie.trailer_url,
        # background_url=movie.background_url,
        del_yn=movie.del_yn,
        actors=actors_response
    )

def parse_cast_info(cast_info):
    """
    전체 배우 정보를 문자열로 받아 배우 이름과 역할을 분리하여 리스트로 반환하는 함수.
    
    Parameters:
    - cast_info (str): '배우1 (역할), 배우2 (역할)' 형태의 문자열
    
    Returns:
    - List[Tuple[actor_name, role]] 형태의 리스트 반환
    """
    # 괄호의 짝을 맞추고 괄호 밖의 쉼표로 분리
    actor_list = []
    buffer = ''
    open_paren = 0  # 괄호가 열려 있는지 확인하는 변수

    for char in cast_info:
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

    # 각 배우 정보를 처리하여 (배우 이름, 역할) 형태의 튜플 리스트로 변환
    parsed_actors = []
    for actor_info in actor_list:
        match = re.match(r"(.+?)\s*\((.+)\)", actor_info)
        if match:
            actor_name = match.group(1).strip()  # 배우 이름
            role = match.group(2).strip()  # 괄호 안에 있는 내용 전체를 역할로
        else:
            # 괄호가 없는 경우, 역할 정보가 없으므로 빈 역할로 저장
            actor_name = actor_info.strip()
            role = None
        parsed_actors.append((actor_name, role))  # 튜플 형태로 저장

    return parsed_actors



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

def create_session():
    """
    SQLAlchemy 세션을 생성하고 반환하는 함수.
    """
    user = 'root'
    password = '1234'
    host = '3.36.106.130'
    port = '32263' 
    database = 'moviedb' 
    movieEngine = create_engine(f"mysql+pymysql://{user}:{password}@{host}:{port}/{database}")

    # 세션 생성기
    Session = sessionmaker(bind=movieEngine)
    session = Session()
    return session  # 생성된 세션 객체 반환

def save_movie_data_to_db(movie_title, plot, year, genre, country, image_url, background_image_url, director_list, cast_info, running_time, audience, trailer_url):
    """
    영화 정보를 MySQL 데이터베이스에 저장하고, 응답 객체로 변환하여 response 리스트에 추가하는 함수.
    """
    global response
    try:
        # 세션 생성
        session = create_session()

        # 기존 영화가 존재하는지 확인 (movie_title과 year를 기준으로 조회)
        existing_movie = session.query(DbMovie).filter_by(movie_title=movie_title, movie_year=year).first()

        if existing_movie:
            # 기존 영화의 배우 정보 조회
            existing_movie_seq = existing_movie.movie_seq
            existing_actors = session.query(DbActor).filter_by(movie_seq=existing_movie_seq).all()

            # existing_movie와 관련된 배우 정보를 변환하여 응답에 추가
            response.append(convert_movie_to_response(existing_movie, existing_actors))
            return  # 함수 종료

        # Movie 객체 생성 및 세션에 추가
        movie = DbMovie(
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

        # 배우 정보 파싱 및 처리
        parsed_actors = parse_cast_info(cast_info)  # cast_info 전체 문자열을 파싱하여 배우 정보 리스트 반환
        actor_list = []
        for actor_name, role in parsed_actors:
            # Actor 객체 생성 및 저장
            actor = DbActor(
                actor_name=actor_name,
                role=role,
                movie_seq=movie.movie_seq  # 생성된 movie_seq 사용
            )
            session.add(actor)
            actor_list.append(actor)

        # 최종적으로 한 번의 커밋으로 영화 및 배우 정보 저장
        session.commit()

        # 저장된 Movie와 Actor 리스트를 응답 객체로 변환하여 추가
        response.append(convert_movie_to_response(movie, actor_list))

    except Exception as e:
        print(f"Error saving movie data to database: {e}")
        session.rollback()
    finally:
        session.close()


def collect_movie_titles(driver):
    """
    네이버 검색 페이지에서 영화 제목과 개봉 연도를 수집하여 리스트로 반환하는 함수.
    """
    url = "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query=%EC%9D%B4%EB%B2%88%EB%8B%AC+%EA%B0%9C%EB%B4%89%EC%98%81%ED%99%94&oquery=%EC%9D%B4%EB%B2%88%EC%A3%BC+%EA%B0%9C%EB%B4%89%EC%98%81%ED%99%94&tqi=iYGVgspzL8wssdL3Py0ssssssQo-358768"

    # 영화 제목과 연도를 담을 리스트
    movie_info_list = []

    try:
        driver.get(url)
        time.sleep(3)  # 페이지가 로드될 때까지 대기

        while True:
            soup = BeautifulSoup(driver.page_source, "html.parser")
            card_panel = soup.find("div", class_="card_area _panel")

            if card_panel:
                title_divs = card_panel.find_all("div", class_="title multi_line _ellipsis")

                for title_div in title_divs:
                    a_tag = title_div.find("a")
                    if a_tag:
                        movie_title = a_tag.get_text().strip()

                        parent_div = title_div.find_parent("div", class_="card_area _panel")
                        if parent_div:
                            info_group = parent_div.find("dl", class_="info_group type_visible")
                            movie_year = None
                            if info_group:
                                dd_tags = info_group.find("dd")
                                if dd_tags:
                                    movie_year = dd_tags.get_text().split('.')[0].strip()

                            movie_info_list.append({'title': movie_title, 'year': movie_year})

            try:
                next_button = driver.find_element(By.CSS_SELECTOR, 'a.pg_next._next.on')
                if next_button.get_attribute("aria-disabled") == "true":
                    break

                next_button.click()
                time.sleep(2)

            except Exception:
                break

    except Exception as e:
        print(f"Error during crawling: {e}")

    return movie_info_list

def search_and_find_movie(driver, movie_info_list):
    """
    영화 목록 CSV 파일을 읽어서 각 영화에 대한 정보를 추출하고, 
    성공적으로 처리된 영화의 행은 파일에서 삭제합니다.
    """
    remaining_rows = []
    for movie_info in movie_info_list:
        try:
            movie_title = movie_info.get('title')
            movie_year = movie_info.get('year')
            if(fetch_movie_data(driver, str(movie_title), movie_year) == False):
                remaining_rows.append(movie_info)
        except KeyError:
            remaining_rows.append(movie_info)
        except Exception:
            remaining_rows.append(movie_info)

    return remaining_rows if remaining_rows else []

def fetch_movie_data(driver, movie_title, year):
    """
    영화 제목을 받아 해당 영화를 검색하고, 영화 정보와 리뷰를 추출하여 CSV 파일에 저장하는 함수.
    """
    try:
        search_url = f"https://pedia.watcha.com/ko-KR/search?query={movie_title}"
        driver.get(search_url)
        driver.implicitly_wait(10)
        time.sleep(1)

        html = driver.page_source
        soup = BeautifulSoup(html, "html.parser")
        li_elements = None

        try:
            section = soup.find("section", class_="e3425a05cc8981969106 d34b60d5a027fe9be462")
            if section:
                li_elements = section.find_all("li", class_="c1fbb66dd0a8919a619b ef9348f4092a69aeb892")
            else:
                div_element = soup.find("div", class_="d54b5ff7a8eaf4d71c6d")
                if div_element:
                    return True
                return False
        except AttributeError:
            print(f"Error: Unable to find the section or movie list for {movie_title}")
            return False

        if li_elements:
            for li in li_elements:
                try:
                    a_tag = li.find("a", class_="InnerPartOfListWithImage e243b4e8439813c3ed15 ead347fbee413c8c2a9b")
                    if a_tag and a_tag['title'].replace(" ", "").strip() == movie_title.replace(" ", "").strip():
                        movie_url = "https://pedia.watcha.com" + a_tag['href']
                        driver.get(movie_url)
                        driver.implicitly_wait(10)
                        time.sleep(1)

                        movie_html = driver.page_source
                        movie_soup = BeautifulSoup(movie_html, "html.parser")
                        info_elements = movie_soup.find_all("div", class_="acb1a192f548da4feda1 cbf385a8dfd716be802c")

                        if info_elements:
                            movieYear = None
                            genre = None
                            country = None
                            if len(info_elements) > 1:
                                year_genre_country = info_elements[1].text.strip().split('·')
                                if len(year_genre_country) > 2:
                                    movieYear = year_genre_country[0].strip()
                                    genre = year_genre_country[1].strip()
                                    country = year_genre_country[2].strip()
                            running_time = None
                            audience = None
                            if len(info_elements) > 2:
                                running_time_audience = info_elements[2].text.strip().split('·')
                                if len(running_time_audience) > 1:
                                    running_time = running_time_audience[0].strip()
                                    audience = running_time_audience[1].strip()

                            if movieYear == year:
                                write_movie_data(movie_soup, movie_title, genre, country, running_time, audience, year)
                                return True
                except Exception as e:
                    print(f"Error processing movie: {e}", movie_title)
                    continue
            return True
    except Exception as e:
        print(f"Error fetching movie data: {e}", movie_title)
        return False

def write_movie_data(movie_soup, movie_title, genre, country, running_time, audience, year):
    """
    영화 정보를 MySQL 데이터베이스에 저장하는 함수.
    """
    try:
        cast_info_section = movie_soup.find("div", class_="cc6c6c81887bf112cdd4 listWrapper")
        cast_info = []
        director_list = []

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

        plot_element = movie_soup.find("p", class_="cbf385a8dfd716be802c")
        plot = None
        if plot_element:
            plot = plot_element.text.strip()

        image_element = movie_soup.find("div", class_="e986acb87595fabe84dc a687e116ed254fa2f1fd dc9c3d0a66c0affbade8")
        image_url = None
        if image_element:
            image_url = image_element.find("img")["src"]

        background_image_element = movie_soup.find("div", class_="d5d376293ff34390f11f")
        background_image_url = None
        if background_image_element:
            style_attribute = background_image_element.get("style")
            if 'url(' in style_attribute:
                background_image_url = style_attribute.split('url(')[1].split(')')[0].strip('"\'')
                
        trailer_url = None
        ul_tag = movie_soup.find("ul", class_="d66339e2b69adf69b22f e47cb6a241fb8eb3c810")
        if ul_tag:
            li_elements = ul_tag.find_all("li", class_="c5c4891385919a827406 db96a9bd17142a5de66d")
            for li in li_elements:
                a_tag = li.find("a", class_="dee9cb333a0540526ecb")
                if a_tag:
                    trailer_url = a_tag["href"]
                    break

        save_movie_data_to_db(movie_title, plot, year, genre, country, image_url, background_image_url, director_list, cast_info, running_time, audience, trailer_url)

    except Exception as e:
        print(f"Error writing movie data: {e}: ", movie_title)

def decode_trailer_url(encoded_url):
    """
    URL-safe base64로 인코딩된 문자열을 디코딩하여 실제 유튜브 링크로 변환하는 함수.
    """
    if encoded_url is None:
        return None

    try:
        encoded_part = encoded_url.split('/')[-1]
        encoded_part = encoded_part.replace('-', '+').replace('_', '/')
        missing_padding = len(encoded_part) % 4
        if missing_padding != 0:
            encoded_part += '=' * (4 - missing_padding)
        decoded_url = base64.b64decode(encoded_part).decode('utf-8')
        return decoded_url.replace("watch?v=", "embed/")
    except Exception as e:
        print(f"Error decoding trailer URL '{encoded_url}': {e}")
        return None

def main_process(driver):
    """
    메인 크롤링 및 데이터베이스 저장 프로세스를 수행하는 함수.
    """
    movie_info_list = collect_movie_titles(driver)
    while movie_info_list:
        movie_info_list = search_and_find_movie(driver, movie_info_list)

@app.get("/api/newMovie/list", response_model=List[MovieResponse])
def getNewMovieList():
    """
    영화 데이터를 크롤링하고 수집한 후 영화 ID 리스트를 JSON으로 반환하는 API 엔드포인트.
    """
    global response
    driver = None
    try:
        driver = initialize_driver()  # Selenium WebDriver 초기화
        main_process(driver)
        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
    finally:
        if driver is not None:
            driver.quit()

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8002)
