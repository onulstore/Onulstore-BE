![logo](https://user-images.githubusercontent.com/77063888/197363644-48e4ede5-8331-4e04-8646-bac58f17a105.jpg)

## Project Overview

### Project Duration

`2022.09.01 ~ 2022.10.14`

### Project Theme

- 오늘상점 쇼핑몰의 '오늘'이라는 브랜드 특색을 살리고,
  제품 큐리이션을 '콘텐츠'로 만드는 모바일웹 리뉴얼 프로젝트 입니다.

### Project Concept

- 오늘상점의 물건들을 '공급자 관점'이 아닌 '소비자 관점'으로 접근했습니다.
- 오늘상점에서 제공하는 상품을 아이템으로 만드는 '특별한 스토리'를 매일 새롭게 전달하고자 합니다.

### URL

`Client` : https://onulstore.netlify.app/ <br>
`API Server` : https://onulstore.breon.ml/

`Front-End Repository` : <br>

- [Onulstore-FE](https://github.com/onulstore/Onulstore-FE)<br>
- [Onulstore-FE-admin](https://github.com/onulstore/Onulstore-FE-admin)
  <br>

## Team

### UXUI

|심예진<br>(UXUI 팀장)|최송현|최희경|
|:----:|:----:|:----:|

### Front-End

|김명진<br>(FE 팀장)|이화정|김정환|박지훈|
|:----:|:----:|:----:|:----:|
|[@k-m-jin](https://github.com/k-m-jin)|[@Hwa-J](https://github.com/Hwa-J)|[@Jung-Hwan-Kim-97](https://github.com/Jung-Hwan-Kim-97)|[@hoona1011](https://github.com/hoona1011)|

### Back-End

|이원근<br>(BE 및 전체 팀장)|여병규|정아름|
|:----:|:----:|:----:|
|[@leewg97](https://github.com/leewg97)|[@diaboloss217](https://github.com/diaboloss217)|[@army246](https://github.com/army246)|
|<img width="100" height="100" alt="image" src="https://user-images.githubusercontent.com/77063888/197363851-20b70139-4b2a-4615-baea-1695250e5c3f.jpg">|<img width="100" height="100" alt="image" src="https://user-images.githubusercontent.com/77063888/197363846-d309ab48-4236-49d6-85f8-1cfa287ba4fa.jpg">|<img width="100" height="100" alt="image" src="https://user-images.githubusercontent.com/77063888/197363849-ab42e7c2-fa78-4761-b3d7-5c373937ccca.jpg">|

## Back-End 역할 분담

`이원근` : 서버 구축(EC2, RDS, Route 53), CI/CD, Docker, 인증, 회원, 브랜드, 카테고리, 큐레이션, 공지, 주문, 결제<br>
`여병규` : AWS S3, 관리자 기능 내에 토픽 별 대시보드, 장바구니, 쿠폰, 상품<br>
`정아름` : 질문, 답변, 리뷰, 찜

<br>

## Tech Stack

![Tech Stack](https://user-images.githubusercontent.com/77063888/197366791-01e0fb5e-d2b4-478e-b617-c8f4b37a4083.png)

### Development Environment

`IDE` : IntelliJ IDEA <br>
`Java` : Jdk 11 <br>
`Spring Boot` : 2.7.3 <br>
`Gradle` : 7.5 <br>
`DataBase` : MySQL 8.0.28 <br>
`AWS EC2` : <br>

- Jenkins Server : Ubuntu<br>
- API Server : Amazon Linux 2 Kernel 5.10 <br>

<br>

## Service Architecture

![Service Architecture](https://user-images.githubusercontent.com/77063888/197367655-6565313d-73a6-46ec-ab96-c821ee00aa87.png)

- Jenkins Server와 API Server, 총 2개의 EC2를 사용합니다.
- 두 개의 EC2 인스턴스에는 CI/CD를 위한 Jenkins Container와 Main Server인 Spring Container로 각각 하나씩 존재합니다.

### CI/CD 과정

- 각자의 브랜치에서 작업 완료 후 `develop` 브랜치로 Pull Request를 작성합니다.
- 본인을 제외한 팀원이 Pull Request을 확인한 후 이상 없으면 승인 및 Merge 합니다.
- 배포가 가능한 정도의 작업물이 모였을 때 `develop` 브랜치에서 `master` 브랜치로 Pull Request 후 위 과정 반복합니다.
- 이때 GitHub Webhook을 통해 Jenkins에게 push event 알리게 되고, 이후 Jenkins Pipeline을 거치게 됩니다.
    - Git Repository Clone
    - Jenkins workspace에 있는 `배포용 application.yml`로 변경 후 Gradle Build
    - 생성된 JAR File로 Docker Image Build
    - 생성된 Image를 Docker Hub로 Push
    - Docker Hub로부터 Image를 Pull 하여 해당 이미지를 기반으로 Container Run

## ER Diagram

![ERD](https://user-images.githubusercontent.com/77063888/197365654-89e39c46-5c01-4f64-8065-bb1ff00aff02.png)

## API 명세서

[Onulstore API 명세서](https://documenter.getpostman.com/view/21774748/2s83zjt3ti)

## Result