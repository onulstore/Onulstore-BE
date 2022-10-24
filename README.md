<img alt="logo" src="https://user-images.githubusercontent.com/77063888/197363644-48e4ede5-8331-4e04-8646-bac58f17a105.jpg">

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

`이원근` : 
- 서버(AWS EC2, RDS, Route 53), CI/CD(Jenkins, Docker)
- 인증, 회원, 브랜드, 카테고리, 큐레이션, 공지, 주문, 결제

`여병규` : 
- 서버(AWS S3)
- 대시보드, 장바구니, 쿠폰, 상품

`정아름` : 
- 질문, 답변, 리뷰, 찜

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

- Jenkins Server : AWS Ubuntu 22.04<br>
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

<br>

## ER Diagram

![ERD](https://user-images.githubusercontent.com/77063888/197365654-89e39c46-5c01-4f64-8065-bb1ff00aff02.png)

## Package Structure

<details>
<summary>Onulstore-BE</summary>
<div markdown="1">

```
 src
  ├─ main
  │  ├─ java
  │  │  └─ com
  │  │     └─ onulstore
  │  │        ├─ common
  │  │        ├─ config
  │  │        │  ├─ auth 
  │  │        │  ├─ exception
  │  │        │  ├─ jwt
  │  │        │  └─ oauth2
  │  │        ├─ domain[Entity + Repository]
  │  │        ├─ service
  │  │        └─ web
  │  │           ├─ controller
  │  │           └─ dto
  │  └─ resources
  └─ test
```

</div>
</details>

<br>

## API 명세서

[Onulstore API 명세서](https://documenter.getpostman.com/view/21774748/2s83zjt3ti)

<br>

<details>
<summary>관리자 관련</summary>
<div markdown="1">

- `POST`
    - `/admin/signup` : 입점사 회원가입
    - `/admin/dashboard` : 전체 대시보드 / 관리자
    - `/admin/dashboard/categories` : 카테고리 대시보드 / 관리자
    - `/admin/dashboard/customers` : 질문 및 리뷰 대시보드 / 관리자
    - `/admin/dashboard/dailyStatistic` : 대시보드 통계 / 관리자
    - `/admin/dashboard/members` : 회원 대시보드 / 관리자
    - `/admin/dashboard/orders` : 판매금액 아이템 수 대시보드 / 관리자
    - `/admin/dashboard/patmentOrders` : 주문 관련 대시보드 / 관리자
    - `/admin/dashboard/products` : 상품 대시보드 / 관리자
- `GET`
    - `/admin/find` : 전체 회원 조회 / 관리자

</div>
</details>

<details>
<summary>인증 관련</summary>
<div markdown="1">

- `POST`
    - `/auth/signup` : 회원가입
    - `/auth/login` : 로그인
    - `/auth/refresh` : Refresh Token 발급
    - `/auth/find/email` : 휴대폰 번호로 이메일 찾기

</div>
</details>

<details>
<summary>브랜드 관련</summary>
<div markdown="1">

- `POST`
    - `/brands` : 브랜드 등록 / 관리자
- `GET`
    - `/brands` : 브랜드 전체 조회
    - `/brands/{brandId}/product` : 브랜드 상품 조회
- `PUT`
    - `/brands/{brandId}` : 브랜드 수정 / 관리자
- `DELETE`
    - `/brands/{brandId}` : 브랜드 삭제 / 관리자

</div>
</details>

<details>
<summary>장바구니 관련</summary>
<div markdown="1">

- `POST`
    - `/carts` : 장바구니 등록
    - `/carts/{cartId}/plus` : 장바구니 수량 증가
    - `/carts/{cartId}/minus` : 장바구니 수량 감소
- `GET`
    - `/carts` : 장바구니 조회
- `DELETE`
    - `/carts/{cartId}` : 장바구니 삭제

</div>
</details>

<details>
<summary>카테고리 관련</summary>
<div markdown="1">

- `POST`
    - `/categories` : 카테고리 등록 / 관리자
- `GET`
    - `/categories` : 카테고리 전체 조회
    - `/categories/{categoryId}/product` : 카테고리 상품 조회
- `PUT`
    - `/categories/{categoryId}` : 카테고리 수정 / 관리자
- `DELETE`
    - `/categories/{categoryId}` : 카테고리 삭제 / 관리자

</div>
</details>

<details>
<summary>쿠폰 관련</summary>
<div markdown="1">

- `POST`
    - `/coupon` : 특정 유저에게 쿠폰 등록 / 관리자
    - `/coupons` : 모두에게 쿠폰 등록 / 관리자
- `GET`
    - `/mycoupons` : 쿠폰 조회

</div>
</details>

<details>
<summary>큐레이션 관련</summary>
<div markdown="1">

- `POST`
    - `/curations/magazine` : 매거진 등록 / 관리자
    - `/curations/recommend` : MD추천 등록 / 관리자
    - `/curations/{curationId}/image` : 큐레이션 이미지 등록 / 관리자
- `GET`
    - `/curations` : 큐레이션 조회
    - `/curations/{curationId}` : 특정 큐레이션 조회
    - `/curations/magazine` : 매거진 전체 조회
    - `/curations/recommend` : MD추천 전체 조회
- `PUT`
    - `/curations/{curationId}` : 큐레이션 수정 / 관리자
    - `/curations/{curationId}/display` : 공개 여부 TRUE / 관리자
    - `/curations/{curationId}/unDisplay` : 공개 여부 FALSE / 관리자
- `DELETE`
    - `/curations/{curationId}` : 큐레이션 삭제 / 관리자

</div>
</details>

<details>
<summary>회원 관련</summary>
<div markdown="1">

- `GET`
    - `/members` : 내 정보
    - `/members/latest` : 최근 본 상품
- `PUT`
    - `/members` : 프로필 수정
    - `/members/password` : 비밀번호 수정
- `DELETE`
    - `/members` : 회원 탈퇴

</div>
</details>

<details>
<summary>공지 관련</summary>
<div markdown="1">

- `POST`
    - `/notices` : 공지 등록 / 관리자
    - `/notices/banner` : 홈 배너 내용 등록 / 관리자
    - `/notices/{noticeId}/image` : 공지 내용(이미지) 업로드 / 관리자
- `GET`
    - `/notices` : 공지 조회
    - `/notices/{noticeId}` : 특정 공지 조회
- `PUT`
    - `/notices/{noticeId}` : 공지 수정 / 관리자
    - `/notices/{noticeId}/banner` : 홈 배너 내용 수정 / 관리자
- `DELETE`
    - `/notices/{noticeId}` : 공지 삭제 / 관리자

</div>
</details>

<details>
<summary>주문 관련</summary>
<div markdown="1">

- `POST`
    - `/orders` : 단일 상품 주문
    - `/orders/cartorder` : 장바구니 상품 선택 주문
- `GET`
    - `/orders` : 주문 및 결제 내역 조회
    - `/orders/{orderId}` : 특정 주문의 주문 및 결제 내역 조회
    - `/orders/entire` : 전체 주문 및 결제 내역 조회 / 관리자
- `PUT`
    - `/orders` : 주문 상태 변경(환불 요청 / 구매 확정)
    - `/orders/{orderId}` : 환불 완료 / 관리자
    - `/orders/update` : 해당 주문 회원 정보 변경
- `DELETE`
    - `/orders/{orderId}` : 브랜드 삭제

</div>
</details>

<details>
<summary>결제 관련</summary>
<div markdown="1">

- `POST`
    - `/payments` : 결제 하기

</div>
</details>

<details>
<summary>상품 관련</summary>
<div markdown="1">

- `POST`
    - `/products` : 상품 등록 / 관리자
    - `/products/search` : 상품 검색
    - `/products/discount/{productId}` : 상품 할인 추가 / 관리자
    - `/products/{productId}/content` : 상품 설명 업로드 / 관리자
    - `/products/{productId}/image` : 상품 이미지 업로드 / 관리자
- `GET`
    - `/products/list` : 상품 전체 조회
    - `/products/{productId}` : 상품 상세 조회
- `PUT`
    - `/products/{productId}` : 상품 수정 / 관리자
- `DELETE`
    - `/products/{productId}` : 상품 삭제 / 관리자
    - `/products/{productId}/image` : 상품 이미지 삭제 / 관리자

</div>
</details>

<details>
<summary>질문 관련</summary>
<div markdown="1">

- `POST`
    - `/questions` : 질문 등록
- `GET`
    - `/members/questions` : 질문 전체 조회(멤버별)
    - `/questions/{productId}` : 질문 전체 조회(상품별)
    - `/questions/{productId}/{questionId}` : 질문 상세 조회
- `PUT`
    - `/questions/{questionId}` : 질문 수정
- `DELETE`
    - `/questions/{questionId}` : 질문 삭제

</div>
</details>

<details>
<summary>답변 관련</summary>
<div markdown="1">

- `POST`
    - `/questions/{questionId}/answers` : 답변 등록 / 관리자
- `GET`
    - `/questions/{questionId}/answers/{answerId}` : 답변 조회

</div>
</details>

<details>
<summary>리뷰 관련</summary>
<div markdown="1">

- `POST`
    - `/reviews/{orderId}` : 리뷰 등록
    - `/reviews/{reviewId}/image` : 리뷰 이미지 등록
- `GET`
    - `/members/reviews` : 리뷰 목록 조회(멤버별)
    - `/products/{productId}/reviews` : 리뷰 목록 조회(상품별)
    - `/reviews/{reviewId}` : 리뷰 상세 조회
- `PUT`
    - `/reviews/{reviewId}` : 리뷰 수정
- `DELETE`
    - `/reviews/{reviewId}` : 리뷰 삭제

</div>
</details>

<details>
<summary>찜 관련</summary>
<div markdown="1">

- `POST`
    - `/wishlists` : 찜 등록
- `GET`
    - `/wishlists` : 찜 조회
- `DELETE`
    - `/wishlists/{productId}` : 찜 삭제

</div>
</details>

<br>

## Result
