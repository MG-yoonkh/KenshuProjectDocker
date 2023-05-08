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
 1. jdk17の設置 : OracleのJDK17をダウンロードし、インストールする。  
 [java17ダウンロード](https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe)
 3. PATH設定 : Windowsのシステム詳細設定 - 環境変数 - システム環境変数の新規 - 変数名：JAVA_HOME、変数値：JAVAのディレクトリを入力 - OK - システム環境変数のpathをダブルクリック - 新規 - %JAVA_HOME%\bin\ を入力 - 上へをクリックし、一番上まで上げる - ok
 4. MySQL8.0設置 : mysqlのダウンロードページに接続 - 8.0.32バージョンのインストーラーをダウンロード - インストールする。  
 [MySQL8.0ダウンロード](https://downloads.mysql.com/archives/installer/)
 6. IntelliJ設置 : IntelliJのダウンロードページに接続 - Ultimate(有料) or Community(無料)をダウンロード - インストールする。  
 [IntelliJダウンロード](https://www.jetbrains.com/idea/download/#section=windows)
 8. MySQLWorkbenchを開き、新規コネクションを作って'Recipe' Databaseを生成する。(CREATE DATABASE Recipe;)
 9. プロジェクトをzipファイルでダウンロードし、管理しやすい経路に展開  
 [プロジェクトzipファイル](https://github.com/MG-yoonkh/kenshu/archive/refs/heads/main.zip)
 10. IntelliJを開けて、recipe - build.gradleを開ける。
 11. IntelliJのFile - project structureでJAVAのバージョンを17に設定
 <details>
<summary>もっと見る</summary>
<div markdown="1">   
 <center>
    <img src="https://user-images.githubusercontent.com/125540360/236724252-e96eaa71-cec2-4cf1-8d90-444b462e637d.png" alt=""  >
    <img src="https://user-images.githubusercontent.com/125540360/236724020-8a2a4a3b-acb2-4086-8d01-f2f2e8c061ad.png" alt="" >
<center>
</div>
</details>
 12. recipe - src - main - java - RecipeApplicationを実行する。
 13. DATAフォルダのcsvファイルをMySQLにインポートする。
 14. localhost:8080に接続する。    



* Dockerを使う場合
 1. Docker desktopを設置する  
 [Docker desktop ダウンロード](https://www.docker.com/products/docker-desktop/)
 3. Docker desktopを実行する。
 4. プロジェクトをzipファイルでダウンロードし、管理しやすい経路に展開  
 [プロジェクトzipファイル](https://github.com/MG-yoonkh/kenshu/archive/refs/heads/main.zip)
 6. プロジェクトのzipファイルを展開し、kenshu-mainフォルダの内のrecipeフォルダでcmdを開け、docker-compose up -dを入力する。
 7. もう一度docker-compose up -dを入力し、docker-compose exec db mysql -u rootを入力する。(DB初期化作業)
 8. dataフォルダのinit.sqlを開き、内容を全部コピーしてcmdにペーストし、エンターキーを押す。
 9. exitを入力して、もう一度docker-compose down、docker-compose upを入力する。
 10. ブラウザを開き、localhost:8080に接続する。
 


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
