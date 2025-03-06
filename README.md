# 🎯 목표
- Spring Security 및 JWT를 사용해 인증/인가에 대한 개념을 학습하기
- Access Token 및 Refresh Token 구현하기
- Cookie 활용하기
- JWT 검증 및 Access Token 갱신하기

# 🛠 기술 스택
- IDE: IntelliJ
- 언어: Java 21
- 프레임워크: Spring Boot 3.4.3
- 인증/인가: Spring Security
- 데이터베이스: MySQL

# 📄 API 명세서

## 회원가입

### Request
- Method : POST
- URL : /users/signup
- Content-Type : application/json
- Authorization : -

|필드명|타입|필수 여부|설명|
|---|---|:---:|---|
|email|String|Y|이메일|
|password|String|Y|비밀번호|
|nickname|String|Y|닉네임|
|role|String|Y|권한|

```
{
    "nickname": "jiyeon",
    "role": "admin",
    "email": "user1@naver.com",
    "password": "User123*"
}
```

### Response
|상태 코드|설명|
|---|---|
|201 Created|회원가입 성공|
|400 Bad Request|잘못된 요청을 보낼 경우|
|409 Conflict|동일한 이메일이 존재할 경우|

```
{
    "id": 1,
    "email": "user1@naver.com",
    "nickname": "jiyeon",
    "role": "ADMIN",
    "createdAt": "2025-03-07T05:19:51.8820138",
    "modifiedAt": "2025-03-07T05:19:51.8820138"
}
```
```
{
    "timestamp": "2025-03-07T05:20:07.7251269",
    "status": 409,
    "error": "Conflict",
    "code": "EMAIL_EXISTS",
    "message": "이미 존재하는 이메일입니다."
}
```

## 사용자 단 건 조회
## 사용자 전체 조회
## 회원탈퇴
## 로그인
## 로그아웃
## 관리자
## Access Token 갱신
