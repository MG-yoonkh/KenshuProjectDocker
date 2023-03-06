import sys
import pandas as pd
import requests
import matplotlib.pyplot as plt
import datetime
from bs4 import BeautifulSoup
import LogClass as log

# 메인메소드 활용, 클래스화, 유효성검사(영어 등)

class finance:


    # 会社のコード、開始日入力
    code = sys.argv[1]
    date = sys.argv[2]


    def __init__(self, code, date):
        self.code = code
        self.date = date



    def last_page(self):
        # NAVERの証券ページ接近
        url = 'https://finance.naver.com/item/sise_day.naver?code={code}'.format(code=self.code)
        res = requests.get(url,headers={'User-Agent': 'Mozilla/5.0'})
        res.encoding = 'euc-kr'

        soap = BeautifulSoup(res.text, 'lxml')

        # 最後のページ出力
        el_table_navi = soap.find("table", class_="Nnavi")
        el_td_last = el_table_navi.find("td", class_="pgRR")
        pg_last = el_td_last.a.get('href').rsplit('&')[1]
        pg_last = pg_last.split('=')[1]
        pg_last = int(pg_last)
        return pg_last




    # ページをParsingしてdfに格納
    def parse_page(code, page):
        try:
            url = 'http://finance.naver.com/item/sise_day.nhn?code={code}&page={page}'.format(code=code, page=page)
            res = requests.get(url,headers={'User-Agent': 'Mozilla/5.0'})
            _soap = BeautifulSoup(res.text, 'lxml')
            _df = pd.read_html(str(_soap.find("table")), header=0)[0]
            _df = _df.dropna()
            return _df
        except:
            log.Log()
        return None




    df = None

    arr = date.split('-')
    try:
        str_datefrom = datetime.datetime.strftime(datetime.datetime(year=int(arr[0]), month=int(arr[1]), day=int(arr[2])), '%Y.%m.%d')
        str_dateto = datetime.datetime.strftime(datetime.datetime.today(), '%Y.%m.%d')

        for page in range(1, last_page()+1):
            _df = parse_page(code, page)
            _df_filtered = _df[_df['날짜'] > str_datefrom]
            if df is None:
                df = _df_filtered
            else:
                df = pd.concat([df, _df_filtered])
            if len(_df) > len(_df_filtered):
                break
    except Exception as e:
        log.Log()


    # カラム名を英語に変更
    df.rename(columns={"날짜": "Date"}, inplace=True)
    df.rename(columns={"종가": "Close"}, inplace=True)
    df.rename(columns={"거래량": "Volume"}, inplace=True)

    # カラムを選んで出力
    data = df.iloc[:, [0, 1, 6]]

    # 日付を逆順に出力
    data = data.sort_values(by=['Date'], axis=0, ascending=True)

    # Consoleに出力
    print(data.to_string(index=False))

    # matplotlibを利用してグラフを作成
    fig = plt.figure()

    plt.title('Samsung Electronics')
    ax1 = fig.add_subplot(2, 1, 1)
    ax2 = fig.add_subplot(2, 1, 2)

    ax1.plot(data["Date"], data["Close"], color="navy", linewidth=3)
    ax2.plot(data["Date"], data["Volume"], color="green", linewidth=3)


    plt.show()


    # csvファイルにセーブ
    data.to_csv('finance.csv')