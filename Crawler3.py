import sys
import traceback
import pandas as pd
import requests
import matplotlib.pyplot as plt

code = sys.argv[1]
date = sys.argv[2]

url = 'https://finance.naver.com/item/sise_day.naver?code={code}'.format(code=code)
res = requests.get(url,headers={'User-Agent':'Mozilla/5.0'})
res.encoding = 'euc-kr'


from bs4 import BeautifulSoup
soap = BeautifulSoup(res.text, 'lxml')

el_table_navi = soap.find("table", class_="Nnavi")
el_td_last = el_table_navi.find("td", class_="pgRR")
pg_last = el_td_last.a.get('href').rsplit('&')[1]
pg_last = pg_last.split('=')[1]
pg_last = int(pg_last)


def parse_page(code, page):
    try:
        url = 'http://finance.naver.com/item/sise_day.nhn?code={code}&page={page}'.format(code=code, page=page)
        res = requests.get(url,headers={'User-Agent':'Mozilla/5.0'})
        _soap = BeautifulSoup(res.text, 'lxml')
        _df = pd.read_html(str(_soap.find("table")), header=0)[0]
        _df = _df.dropna()
        return _df
    except Exception as e:
        traceback.print_exc()
    return None

df = None

for page in range(1, pg_last+1):
    _df = parse_page(code, page)
    _df_filtered = _df[_df['날짜'] > date]
    if df is None:
        df = _df_filtered
    else:
        df = pd.concat([df, _df_filtered])
    if len(_df) > len(_df_filtered):
        break

print(df)


fig = plt.figure()

ax1 = fig.add_subplot(2,1,1)
ax2 = fig.add_subplot(2,1,2)

ax1.plot(df["날짜"],df["종가"], color="navy" , linewidth = 3)
ax2.plot(df["날짜"],df["거래량"], color="green" , linewidth = 3)

plt.show()


df.to_csv('finance.csv', columns=['날짜', '종가', '시가', '거래량'])