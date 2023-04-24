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
 1. jdk17の設置 : OracleのJDK17をダウンロード ( https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe )し、インストールする。
 2. PATH設定 : Windowsのシステム詳細設定 - 環境変数 - システム環境変数の新規 - 変数名：JAVA_HOME、変数値：JAVAのディレクトリを入力 - OK - システム環境変数のpathをダブルクリック - 新規 - %JAVA_HOME%\bin\ を入力 - 上へをクリックし、一番上まで上げる - ok
 3. MySQL8.0設置 : mysqlのダウンロードページに接続 - 8.0.32バージョンのインストーラーをダウンロード(https://downloads.mysql.com/archives/installer/) - インストールする。
 4. IntelliJ設置 : IntelliJのダウンロードページに接続(https://www.jetbrains.com/idea/download/#section=windows) - Ultimate(有料)&Community(無料)をダウンロード - インストールする。
 5. MySQLWorkbenchを開き、新規コネクションを作って'Recipe' Databaseを生成する。(CREATE DATABASE Recipe;)
 6. プロジェクトをzipファイルでダウンロードし、管理しやすい経路に展開
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
   
***

# 6. MySQL EER Diagram

![image](https://user-images.githubusercontent.com/125540360/233840943-cb039ec4-6206-4e5a-9776-bdad1ac47a6e.png)
