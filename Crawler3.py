import sys
import traceback
import pandas as pd
import requests
import matplotlib.pyplot as plt
import logging
import datetime

# 로그 남기기
# logger instance 생성
logger = logging.getLogger()

# formatter 생성
formatter = logging.Formatter('[%(asctime)s][%(levelname)s|%(filename)s:%(lineno)s] >> %(message)s')

# handler 생성 (stream, file)
streamHandler = logging.StreamHandler()
fileHandler = logging.FileHandler('./test.log')

# logger instance에 fomatter 설정
streamHandler.setFormatter(formatter)
fileHandler.setFormatter(formatter)

# logger instance에 handler 설정
logger.addHandler(streamHandler)
logger.addHandler(fileHandler)

# logger instnace로 log 찍기
logger.setLevel(level=logging.WARNING)


# 코드와 날짜 입력받기
code = sys.argv[1]
date = sys.argv[2]

# 네이버증권 일별시세 페이지 접근
url = 'https://finance.naver.com/item/sise_day.naver?code={code}'.format(code=code)
res = requests.get(url,headers={'User-Agent':'Mozilla/5.0'})
res.encoding = 'euc-kr'


from bs4 import BeautifulSoup
soap = BeautifulSoup(res.text, 'lxml')

# 마지막페이지 추출
el_table_navi = soap.find("table", class_="Nnavi")
el_td_last = el_table_navi.find("td", class_="pgRR")
pg_last = el_td_last.a.get('href').rsplit('&')[1]
pg_last = pg_last.split('=')[1]
pg_last = int(pg_last)

# 페이지 파싱해서 df에 저장
def parse_page(code, page):
    try:
        url = 'http://finance.naver.com/item/sise_day.nhn?code={code}&page={page}'.format(code=code, page=page)
        res = requests.get(url,headers={'User-Agent':'Mozilla/5.0'})
        _soap = BeautifulSoup(res.text, 'lxml')
        _df = pd.read_html(str(_soap.find("table")), header=0)[0]
        _df = _df.dropna()
        return _df
    except Exception as e:
        logger.error("error!!!")
    return None

df = None

arr = sys.argv[2].split('-')
try:
    str_datefrom = datetime.datetime.strftime(datetime.datetime(year=int(arr[0]), month=int(arr[1]), day=int(arr[2])), '%Y.%m.%d')
    str_dateto = datetime.datetime.strftime(datetime.datetime.today(), '%Y.%m.%d')

    for page in range(1, pg_last+1):
        _df = parse_page(code, page)
        _df_filtered = _df[_df['날짜'] > str_datefrom]
        if df is None:
            df = _df_filtered
        else:
            df = pd.concat([df, _df_filtered])
        if len(_df) > len(_df_filtered):
            break
except Exception as e:
    logger.error("error!!!")

# 콘솔창에 출력
print(df)

# matplotlib 이용해서 그래프 그리기
fig = plt.figure()

ax1 = fig.add_subplot(2,1,1)
ax2 = fig.add_subplot(2,1,2)

ax1.plot(df["날짜"],df["종가"], color="navy" , linewidth = 3)
ax2.plot(df["날짜"],df["거래량"], color="green" , linewidth = 3)

plt.show()


# csv파일 저장
df.to_csv('finance.csv', columns=['날짜', '종가', '시가', '거래량'])



