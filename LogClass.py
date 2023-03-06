import logging

class Log:
    def __init__(self):
        # ログの生成
        # logger instance 生成
        logger = logging.getLogger()
        # formatter 生成
        formatter = logging.Formatter('[%(asctime)s][%(levelname)s|%(filename)s:%(lineno)s] >> %(message)s')
        # handler 生成 (stream, file)
        streamHandler = logging.StreamHandler()
        fileHandler = logging.FileHandler('./FinanceLog.log')
        # logger instanceに fomatter 設定
        streamHandler.setFormatter(formatter)
        fileHandler.setFormatter(formatter)
        # logger instanceに handler 設定
        logger.addHandler(streamHandler)
        logger.addHandler(fileHandler)
        # logger instnaceで log 出力
        logger.setLevel(level=logging.WARNING)

        logger.error("error!!!")
