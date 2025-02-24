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

- HanSarang Delievery는 집에서 간편하게 음식을 주문할 수 있는 배달 서비스 어플리케이션 입니다.
- 원하는 메뉴를 선택하고 주문하여 맛있는 한끼를 즐길 수 있습니다.
- 배달을 이용한 후에는 식당에 대한 리뷰를 남겨 정보를 주고 받을 수 있습니다.

  ### 프로젝트 목적
- 실생활의 행동을 대체하거나 반복적이고 인력이 많이 소요되는 작업을 서비스로 자동화하여 편의성을 증진 시키기 위함
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
    - 기능 :
    - 일정관리, ERD 다이어그램 작성
    
  ### 박태훈
    - 기능 :
    - API 명세서 작성
    
  ### 우정욱
    - 기능 :
    - README 파일 작성

  ### 이종원 
    - 기능 :
    - Redis 캐싱 구현

<br>
<br>

>## 개발 환경
- 버전 및 이슈 관리 : Github
- 협업 : Zep, Notion
- 배포 환경 :
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

>## 주요 기술
- Redis 캐싱
- Elasticsearch
- QueryDSL 


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

>## ERD
<img src="https://spiky-golf-745.notion.site/image/attachment%3A5ba55126-94a4-481d-95b1-9e62924b1332%3Aimage.png?table=block&id=1a3a1939-e26e-803a-a631-f7f32a7ca996&spaceId=a4e85d84-0dd7-4efd-99f6-8a85e4777ecb&width=2000&userId=&cache=v2"/>
<br>


<br>
<br>

>## 서비스 구성 및 실행 방법

