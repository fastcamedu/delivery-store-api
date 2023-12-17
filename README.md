# '상점용 API' 프로젝트
- 배달 상점에서 사용하는 API 서비스
- Role: **`delivery-store-api`**
- Port: **`20001`**

# Tech Stack
- Spring Boot 3
- Spring Data JPA
- Spring Security
- JWT
- Apache Kafka
- MySQL

# 시스템 구성과 흐름
```mermaid
flowchart LR
    delivery-store-app[상점 배달앱 \n port:20000] --> delivery-store-api[배달 고객 API \n port:20001] --> delivery-store[(상점 데이터베이스\ndelivery_store)]
    delivery-store-api <--> kafka[Kafka]
```

# 초기 접속 URL
- http://localhost:20001/hello

# 초기 계정
- ID: test@test.com
- PW: 1111