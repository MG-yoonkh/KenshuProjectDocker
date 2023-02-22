from pandas_datareader import data as pdr
import yfinance as yf
import matplotlib.pyplot as plt

yf.pdr_override()
#samsung = '005930.KS'
print("会社のコードとスタート日を入力してください。：ex) 005930.KS, 2020-01-01")
company = pdr.get_data_yahoo(input(), start=input())
print(company)

print(company.index)
print(company.columns)

plt.plot(company.index, company.Close, 'b')
plt.show()

company.to_csv("finance.csv")