import sys
import pandas as pd
import requests
import matplotlib.pyplot as plt
import datetime
from bs4 import BeautifulSoup
import LogClass as log
import Validation as val

class FinanceData:
    def __init__(self, code, date):
        self.code = code
        self.date = date
        self.url = f'https://finance.naver.com/item/sise_day.naver?code={code}'
        self.res = None
        self.pg_last = None
        self.df = None
        self.data = None
        self.printdata()



    def get_page_count(self):
        try:
            self.res = requests.get(self.url, headers={'User-Agent': 'Mozilla/5.0'})
            self.res.encoding = 'euc-kr'
            soup = BeautifulSoup(self.res.text, 'lxml')
            el_table_navi = soup.find("table", class_="Nnavi")
            el_td_last = el_table_navi.find("td", class_="pgRR")
            pg_last = el_td_last.a.get('href').rsplit('&')[1]
            pg_last = pg_last.split('=')[1]
            self.pg_last = int(pg_last)
        except:
            val.Validator.validate_code(self.code)

    def parse_page(self, page):
        try:
            url = f'https://finance.naver.com/item/sise_day.naver?code={self.code}&page={page}'
            res = requests.get(url, headers={'User-Agent': 'Mozilla/5.0'})
            soup = BeautifulSoup(res.text, 'lxml')
            df = pd.read_html(str(soup.find("table")), header=0)[0]
            df = df.dropna()
            return df
        except:
            val.Validator.validate_code(self.code)
            log.Log()
            return None

    def get_data(self):
        arr = self.date.split('-')
        try:
            str_datefrom = datetime.datetime.strftime(
                datetime.datetime(year=int(arr[0]), month=int(arr[1]), day=int(arr[2])), '%Y.%m.%d')
            str_dateto = datetime.datetime.strftime(datetime.datetime.today(), '%Y.%m.%d')
            for page in range(1, self.pg_last + 1):
                _df = self.parse_page(page)
                _df_filtered = _df[_df['날짜'] > str_datefrom]
                if self.df is None:
                    self.df = _df_filtered
                else:
                    self.df = pd.concat([self.df, _df_filtered])
                if len(_df) > len(_df_filtered):
                    break
        except Exception as e:
            val.Validator.validate_date(self.date)
            log.Log()

    def clean_data(self):
        self.df.rename(columns={"날짜": "Date"}, inplace=True)
        self.df.rename(columns={"종가": "Close"}, inplace=True)
        self.df.rename(columns={"거래량": "Volume"}, inplace=True)
        self.data = self.df.iloc[:, [0, 1, 6]]
        self.data = self.data.sort_values(by=['Date'], axis=0, ascending=True)

        print(self.data.to_string(index=False))

    def printdata(self):
        self.get_page_count()
        self.get_data()
        self.clean_data()

        fig = plt.figure()

        plt.title('Samsung Electronics')
        ax1 = fig.add_subplot(2, 1, 1)
        ax2 = fig.add_subplot(2, 1, 2)

        ax1.plot(self.data["Date"], self.data["Close"], color="navy", linewidth=3)
        ax2.plot(self.data["Date"], self.data["Volume"], color="green", linewidth=3)

        plt.show()


FinanceData(sys.argv[1], sys.argv[2])