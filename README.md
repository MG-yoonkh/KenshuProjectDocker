プロジェクト名 : ミリネレシピ
===


# 1. プロジェクト環境

* Java17
* SpringBoot3.0.5
* MySQL8.0
* IntelliJ
* VSCode

***

# 2. 使用ライブラリー

* Spring Data JPA
* Thymeleaf
* Lombok
* Spring Security
* Jackson-databind

***

# 3. プロジェクトの設置および実行方法

* ローカルに設置する場合
 1. jdk17、 MySQL8.0、IntelliJ 設置。
 2. MySQLに'Recipe' Databaseを生成する。
 3. プロジェクトをzipファイルでダウンロードし、管理しやすい経路に展開
 4. IntelliJを開けて、recipe - build.gradleを開ける。
 5. IntelliJのFile - project structureでJAVAのバージョンを17に設定
 6. recipe - src - main - java - RecipeApplicationを実行する。
 7. DATAフォルダのcsvファイルをMySQLにインポートする。
 8. localhost:8080に接続する。

* Dockerを使う場合
 1. Docker desktopを設置する
 2. Docker desktopを実行する。
 3. プロジェクトをzipファイルでダウンロードし、管理しやすい経路で展開する。
 4. プロジェクトのrecipeフォルダでcmdを実行する。
 5. gradlew.bat clean build を入力する。
 6. docker-compose up --build を入力する。
 7. ブラウザを開き、localhost:8080に接続する。
 


***

# 4. プロジェクトの使用方法

* 非会員の場合
  * レシピ検索、レシピ照会

* 会員の場合
  * レシピ検索、レシピ照会、レシピ投稿、修正、削除、いいね
 
* 管理者の場合
  * 管理者権限付与、会員削除、レシピ削除、接続者グラフ確認
  
***

# 5. チームメンバーおよび参考資料
  * チームメンバー
    * 朴海鎮、尹景湖
  * 参考資料
    * 書籍(점프 투 스프링부트)、Google検索

![image](https://user-images.githubusercontent.com/125540360/233840943-cb039ec4-6206-4e5a-9776-bdad1ac47a6e.png)
