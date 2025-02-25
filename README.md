># 🛵HanSarang Delivery
<p align="center">
  <img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcVNxgR%2FbtsMsOmdqtz%2FvClHwBlBU8dOTGA3kUVKC1%2Fimg.png" alt="HanSarang Delivery Logo"/>
</p>

- 배포 URL :
- Test ID / PW
  - CUSTOMER :
  - OWNER :
  - MANAGER :
  - MASTER :

<br>
<br>

>## 프로젝트 소개
### 소개
- HanSarang Delievery는 집에서 간편하게 음식을 주문할 수 있는 배달 서비스 어플리케이션 입니다.
- 원하는 메뉴를 선택하고 주문하여 맛있는 한끼를 즐길 수 있습니다.
- 배달을 이용한 후에는 식당에 대한 리뷰를 남겨 정보를 주고 받을 수 있습니다.

### 프로젝트 목적
- 실생활의 행동을 대체하거나 인력이 많이 소요되는 작업을 자동화하여 편의성을 증진 시키기 위함
- 마이크로서비스 아키텍처의 도입을 위한 모놀리식 아키텍처 기반의 어플리케이션 설계
- 요구사항을 분석하고 기능/비기능적인 요소를 구체화 할 수 있는 능력 함양
- 팀원과의 협업으로 통합된 어플리케이션을 개발할 수 있는 능력 함양
- 생성형 인공지능 서비스와 연동하여 어플리케이션에 AI기능을 구현할 수 있는 능력 함양

<br>
<br>

>## 팀원 구성

<div align="center">
  <table>
    <tr>
      <th>권길남 ⭐팀장⭐</th>
      <th>박태훈</th>
      <th>우정욱</th>
      <th>이종원</th>
    </tr>
    <tr>
      <th><img src="https://github.com/user-attachments/assets/d135b543-0694-4efe-8aac-cfdafa6b7595"/></th>
      <th><img src="https://github.com/user-attachments/assets/c20ad074-3ee2-48eb-b0c5-697a87c31660"/></th>
      <th><img src="https://github.com/user-attachments/assets/e8ea8e01-d5be-40bb-9bdb-4ac92b7954f8"/></th>
      <th><img src="https://github.com/user-attachments/assets/148e8046-b822-46b0-97d8-018ad0678236"/></th>
    </tr>
  </table>
</div>

>## 역할 분담

  ### 권길남
    - 기능 : 주문 등록/조회/수정/삭제/검색, 주문 메뉴 등록/조회/수정/삭제/검색
    - 일정관리, ERD 다이어그램 작성, ERD 설계, 전체 테스트
    
  ### 박태훈
    - 기능 : 음식점 등록/조회/수정/삭제/검색, 카테고리 등록/수정/삭제/검색
    - API 명세서 작성, swagger 작성, 배포
    
  ### 우정욱
    - 기능 : 메뉴 등록/조회/수정/삭제/검색, 리뷰 등록/조회/수정/삭제/검색
    - README 파일 작성, 테이블 설계, 중간 서비스 관리

  ### 이종원 
    - 기능 : JWT 인증/인가, 유저 등록/조회/수정/삭제/검색, 배송지 등록/조회/수정/삭제/검색 
    - Redis 데이터 캐싱, Elasticsearch 위치 검색 엔진, Google AI Api 응답 저장

<br>
<br>

>## 아키텍처
<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbddc04%2FbtsMw1DJDs2%2F3n5pyVLDyBFRQIipmL1adk%2Fimg.png">


<br>
<br>

>## 개발 환경
- 버전 및 이슈 관리 : Github
- 협업 : Zep, Notion
- 배포 환경 : AWS EC2, CI/CD
- <a href="https://www.notion.so/teamsparta/1992dc3ef5148084bf8de8ba1c0fb77a">팀 컨벤션</a>

<br>
<br>

>## 기술 스택
- **Back-end** : Java 17 + Spring Boot(3.4.2) 
- **Database** : PostgreSQL, H2
- **Authentication** : JWT, Spring Security
- **Cache** : Redis
- **ORM** : Spring Data JPA, QueryDSL
- **Build Tools** : Gradle
- **Others** : Lombok, Thymeleaf, JUnit Spring Boot Test

<br>
<br>

>## 개발 기간
- 전체 개발 기간 : 2025-02-12 ~ 2025-02-25
- 필수기능 구현 : 2025-02-12 ~  2025-02-18
- 리팩토링 및 버그 수정 : 2025-02-19 ~ 2025-02-23
- 문서화 및 배포 : 2025-02-24 ~ 2025-02-25

<br>
<br>

>## 주요 기술
- Redis
  - 모든 도메인의 Search 서비스 및 JWT 인가 과정 캐싱 적용 
  - DB 부하 감소 및 응답 속도 개선


- Elasticsearch
  - 데이터 인덱싱을 통해 역색인 구조로 빠른 검색 가능
  - 사용자가 입력한 일부 문자열 만으로 관련 데이터를 효과적으로 검색할 수 있음.
  - 검색어에 오타가 있더라도 유사한 결과를 찾아주는 기능 제공.
  - RDB 와의 데이터 동기화 API 를 통해 주기적으로 최신 데이터를 엘라스틱 서치에 반영.
  
<br>
<br>


>## 프로젝트 구조

```plaintext
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── hansarangdelivery
│   │   │           ├── domain      # address / ai / category / location 
│   │   │           |   ├── dto     # / menu / order / restaurant / review / user
│   │   │           |   ├── model
│   │   │           |   ├── repository # 데이터베이스와 상호작용하는 레포지토리
│   │   │           |   └── service 
│   │   │           ├── api            # 웹 요청을 처리하는 컨트롤러
│   │   │           ├── config         # 애플리케이션 설정 관련 클래스
│   │   │           ├── global         # 공통 처리 클래스
│   │   │           |   ├── dto
│   │   │           |   ├── exception
│   │   │           |   └── model
│   │   │           ├── payment
│   │   │           |   ├── model
│   │   │           |   ├── repository
│   │   │           |   └── service
│   │   │           └── security       # 인증 / 인가 관련 클래스
│   │   │           |   └── jwt
│   │   │           └── HansarangDeliveryApplication.java
│   │   ├── resources
│   │   │   ├── application.properties # 애플리케이션 설정 파일
│   │   │   └── static                 # 정적 파일 (이미지, JS, CSS 등)
│   ├── test                           # 테스트 클래스들
│   │   ├── java
│   │   │   └── com
│   │   │       └── hansarangdelivery   
│   │   │           ├── aiReponse      
│   │   │           ├── category      
│   │   │           ├── location      
│   │   │           ├── menu      
│   │   │           |   └── service 
│   │   │           ├── order     
│   │   │           ├── restaurant      
│   │   │           ├── review      
│   │   │           |   └── service 
│   │   │           ├── user      
│   │   │           └── HansarangDeliveryApplicationTests.java
│   │   │
├── .gitattributes
├── .gitignore                          # Git에서 추적하지 않을 파일 목록
├── build.gradle                        # Gradle 빌드 설정 파일
├── README.md                           # 프로젝트 설명 파일
└── pull_request_template.md            # Pull Request 문서
```

<br>
<br>

>## API Docs
<a href="https://www.notion.so/teamsparta/1a12dc3ef5148059b47dcedfe1b96b7f?v=1a12dc3ef514803bb45d000c6e5b1147&pvs=4">
API 명세 페이지 이동</a>

<br>
<br>

>## ERD
<img src="https://spiky-golf-745.notion.site/image/attachment%3A5ba55126-94a4-481d-95b1-9e62924b1332%3Aimage.png?table=block&id=1a3a1939-e26e-803a-a631-f7f32a7ca996&spaceId=a4e85d84-0dd7-4efd-99f6-8a85e4777ecb&width=2000&userId=&cache=v2"/>
<br>


<br>
<br>

>## 서비스 구성 및 실행 방법

<br>
<br>

>## 기술적 고민
- <a href="https://wookslog.tistory.com/85">RESTFul 과 SRP 를 고려한 계층 설계 방안</a>
- <a href="https://zapzook.tistory.com/113">Elasticsearch 를 구현하여 검색 성능 향상</a>
- 테이블 연관 관계 정리
  - 객체의 생명주기가 같은 도메인에만 관계를 맺도록 테이블 연관 관계를 수정
  - JOIN 을 최소화하여 데이터 조회 속도 향상
  - 동일한 생명주기의 관계로  트랜잭션 복잡도 저하
  

<br>
<br>

>## 트러블 슈팅
- <a href="https://zapzook.tistory.com/112">캐싱 적용중 발생한 역직렬화  에러</a>
- <a href="https://wookslog.tistory.com/88">서비스 간의 순환참조 해결 방안</a>
  
<br>
<br>

>## 개선목표

<br>
<br>

>## 프로젝트 소감
> 
### 권길남
능력치와 열정이 높은 팀원들을 만나 팀장으로서 열심히 참여해주시는 모든 팀원들에게 감사했습니다. 
저 또한 협업 과정에서 팀장이 해야하는 일들을 알게되고,프로젝트 진행 과정에서 커뮤니티 능력을 향상 시키는 기회가 되었습니다. 
그리고 본인의 문제가 아니더라도 자신의 일처럼 생각하고 해결하려했던 팀 분위기 덕분에 수월하게 프로젝트를 진행할 수 있었습니다. 
뿐만 아니라 개발자로서 고려해야할 원칙들을 최대한 지키려는 과정에서 한층 더 넓은 시야를 가질 수 있었습니다.

<hr>

### 박태훈
오랜만에 팀프로젝트를 하면서 소통의 중요성을 느낄 수 있었던것 같습니다. 
현재 어떤 고민을 가지고 있고 어떤 방식의 해결을 하는게 좋을지 의견을 나누었던게 좋았는데, 
내가 문제라 생각하지 않았던 것이 사실은 문제 였던 경험이나, 
내가 생각한 해결방식과 다른 해결방식들을 들을수 있었던 것이 특히 좋았습니다.

<hr>

### 우정욱
서로의 의견을 공유하고 하나의 팀 프로젝트로 진행할 수 있는 재밌는 시간이었습니다. 
다른 분들이 알고 있는 지식이나 코딩 스타일을 배울 수 있었고 부족한 부분을 조금씩 채워 나갈 수 있었던 시간이었습니다. 
이번 경험을 기반으로 다양한 개발 지식을 더 공부하고 저의 기술로 만들어 나가고 싶습니다. 

<hr>

### 이종원
열정적인 팀원들과 함께 협업을 하며, 동기 부여도 되고 서로 시너지를 낼 수 있어서 좋았습니다. 
또 이전 팀 프로젝트 경험은 팀원들 간의 대화가 많지 않고 각자 기능 개발에 몰두하는 분위기가 강했는데, 
이번 팀에선 트러블슈팅이나 새로운 아이디어가 떠오를 때 즉각적으로 의견을 나누고 논의할 수 있는 자유로운 분위기가 특히 좋았습니다.