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


 1. Docker desktopを設置する  
 [Docker desktop ダウンロード](https://www.docker.com/products/docker-desktop/)
 3. Docker desktopを実行する。
 4. プロジェクトをzipファイルでダウンロードし、管理しやすい経路に展開  
 [プロジェクトzipファイル](https://github.com/MG-yoonkh/kenshu/archive/refs/heads/main.zip)
 6. プロジェクトのzipファイルを展開し、KenshuProjectDockerフォルダの内のrecipeフォルダでcmdを開け、docker-compose up -dを入力する。
 7. もう一度docker-compose up -dを入力し、docker-compose exec db mysql -u root -pを入力、パスワード1111を入力する。(DB初期化作業)
 8. KenshuProjectDocker/recipe/dataフォルダのinit.sqlを開き、内容を全部コピーしてcmdにペーストし、エンターキーを押す。
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
