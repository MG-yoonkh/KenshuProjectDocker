import re
import datetime

class Validator:
    @staticmethod
    def validate_code(code):
        pattern = re.compile("^[0-9]{6}$")
        if not pattern.match(code):
            raise ValueError("6桁の数字を入力してください。")

    @staticmethod
    def validate_date(date):
        try:
            datetime.datetime.strptime(date, '%Y-%m-%d')
        except ValueError:
            raise ValueError("日付の形式を YYYY-MM-DD　のように入力してください。")
