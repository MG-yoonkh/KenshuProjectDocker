from urllib.request import urlopen
from bs4 import BeautifulSoup

html = urlopen("https://finance.naver.com/sise/sise_market_sum.naver?&page=")

bsObject = BeautifulSoup(html, "html.parser")

for link in bsObject.find_all('a'):
    print(link.text.strip(), link.get_text('href'))