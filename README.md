# Crewing(크루잉)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/cde84515-c58a-432e-96ba-fc090f132756/e4c1ed8f-749e-4360-b33f-0def72198bb7/Untitled.png)

## 1. 서비스 소개

**데모 영상 : https://www.youtube.com/watch?v=O9viJRKPLRI**

**대학생들을 위한 연합 동아리 리크루팅 지원 및 후기를 공유할 수 있는 IOS 어플 서비스** 입니다.

대학생 동아리 모집 정보를 통합하여 제공하고 후기까지 한눈에 볼 수 있는 서비스를 제공합니다. 또한 사용자에게 어울리는 동아리를 추천합니다. 대부분의 대학생들이 여러 사이트를 일일이 검색하며 동아리를 지원하는 번거로움을 해결하고자 개발했습니다.

**핵심기능**

- 카테고리별 연합동아리 분류 및 정보 제공 & 개인 맞춤형 연합동아리 추천 알고리즘
- 고유 번호를 통한 연합동아리 회원 인증
- 동아리 리크루팅 과정 지원 및 결과 알림 서비스
- 손쉬운 동아리 등록 및 동아리원 관리
- 인증된 동아리원의 후기 공유

**성과** 

- CI/CD 배포 성공, **[스파르탄 코딩클럽]  All - in 코딩 공모전: 대학 생활 개선편 본선 진출**

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/cde84515-c58a-432e-96ba-fc090f132756/49c99f66-fb0b-44db-a884-c3f2404bb03b/Untitled.png)

## 2. 개발 (2024.05.18~)

### 백엔드

- **github Action, AWS EC2 + RDS + S3, Docker**로 **CICD** 배포 및 DevOps 구축
- **SpringSecurity + JWT + OAuth**를 통한 소셜 로그인 및 이메일 회원가입 구현
- **Redis 캐싱**을 이용한 **이메일 인증** 서비스 구현
- **SSE**를 이용한 실시간 알림 서비스 및 **FCM(Firebase Cloud Message)**를 이용한 푸시 알림 구현
- **QueryDSL, JPA, 페이지네이션**을 적용하여 동아리 추천 알고리즘 구현
- **AOP ControllerAdvice**를 통한 전역 에러 처리
- 각 기능별 테스트 구현**(Junit5)** 및 **SwaggerUI**를 이용한 api 명세서 구현

## 3. 기술 스택🛠️

### 프론트엔드

<img src="https://img.shields.io/badge/swift-F05138?style=for-the-badge&logo=swift&logoColor=white">

<img src="https://img.shields.io/badge/ios-000000?style=for-the-badge&logo=ios&logoColor=white">

---

### 백엔드

<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white">

<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">

<img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white">

<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white">

<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">

<img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white"> 

<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white">

<img src="[https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white](https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white)">

<img src="[https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white](https://img.shields.io/badge/Amazon%20S3-569A31?style=for-the-badge&logo=Amazon%20S3&logoColor=white)">

<img src="https://img.shields.io/badge/firebase-569A31?style=for-the-badge&logo=firebase&logoColor=white">

<img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">

<img src="https://img.shields.io/badge/jpa-17541F?style=for-the-badge&logo=jpa&logoColor=white">

<img src="https://img.shields.io/badge/QueryDSL-8DD6F9?style=for-the-badge&logo=QueryDSL&logoColor=white">

<img src="https://img.shields.io/badge/JWT-FF9A00?style=for-the-badge&logo=JWT&logoColor=white">

### 협업

<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">

<img src="https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">

<img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">

## 3. 아키텍처

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/cde84515-c58a-432e-96ba-fc090f132756/a9354b4c-8dd5-4ae2-8ef3-8a866d197775/Untitled.png)

## 4. 협업 방식

- **깃 & 깃허브 : 깃플로우** **전략**
- Slack, Notion을 이용한 회의 진행 및 진행 상황 공유
- 프론트엔드 - 백엔드 간 api 명세서 공유(Swagger)

## 5. 팀원 소개

| **김민아(PM)** | **임경진(FE)** | **고 수(BE)** | **신은혜(BE)** |
| --- | --- | --- | --- |
| [@kmina02](https://github.com/kmina02) |  | [@bandalgomsu](https://github.com/bandalgomsu) | [@HideOnCodec](https://github.com/HideOnCodec) |
| ![김민아](https://github.com/user-attachments/assets/32993a53-8c8a-45dd-b775-f1e71848a9e7) | ![임경진](https://github.com/user-attachments/assets/8c03da37-d7db-4426-8788-734e544d6d09) | ![고 수](https://github.com/user-attachments/assets/3f5b869d-48b6-4bd3-b982-c777853c4bd1) | ![신은혜](https://github.com/user-attachments/assets/3c70d156-2ba1-4e83-92b5-75f7ecfa8492) |




