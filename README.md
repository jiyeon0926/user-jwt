# 🌱 배경
Session과 JWT(JSON Web Token)는 인증을 처리하는 웹 애플리케이션의 주요 방식입니다. <br>
Session은 서버에 Session 정보를 저장하고 관리하며, JWT는 클라이언트가 Token을 저장해 인증 정보를 전달합니다. <br>
이번 프로젝트에서는 JWT를 활용해 인증/인가 방식을 구현하여 Session과 JWT의 차이점을 명확히 이해하고자 합니다. <br>
향후 Redis를 사용하지 않은 Refresh Token 구현 방식과 Redis를 활용한 방식의 차이를 비교할 예정입니다.
- [Session을 활용한 사용자 기능 구현](https://github.com/jiyeon0926/user-session)

# 🎯 목표
- Spring Security 및 JWT(JSON Web Token)를 사용해 인증/인가에 대한 개념을 보완하기
- Redis를 사용하지 않고, Refresh Token 구현하기
- JWT 검증 및 Access Token 갱신하기

# 💭 고민한 부분
Access Token을 갱신하려면 Refresh Token이 필요하므로, Refresh Token은 서버 또는 클라이언트에 저장해야 합니다. <br>
Redis를 사용하지 않고, Refresh Token을 어떻게 저장해야 할지 고민됐습니다. <br>
여러 GitHub Repository 및 블로그를 참고한 결과, Cookie를 활용하여 인증하는 방법을 고려하게 되었습니다.
- HttpOnly Cookie는 JavaScript를 통한 접근이 불가능하기 때문에 XSS 공격으로부터 보호 가능
- Cookie는 클라이언트 측에서 관리되므로 서버 부담 감소

# 🛠 기술 스택
- IDE: IntelliJ
- 언어: Java 21
- 프레임워크: Spring Boot 3.4.3
- 인증/인가: Spring Security
- 데이터베이스: MySQL

# 📄 API 명세서

## 1️⃣ 회원가입

### Request
- Method : POST
- URL : /users/signup
- Content-Type : application/json
- Authorization : -

<br>

|필드명|타입|필수 여부|설명|
|---|---|:---:|---|
|email|String|Y|이메일|
|password|String|Y|비밀번호|
|nickname|String|Y|닉네임|
|role|String|Y|권한|

<br>

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

<br>

|필드명|타입|설명|
|---|---|---|
|id|Long|사용자 고유 식별자|
|email|String|이메일|
|nickname|String|닉네임|
|role|String|권한|
|createdAt|LocalDateTime|생성 일자|
|modifiedAt|LocalDateTime|수정 일자|

<br>

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

## 2️⃣ 로그인

### Request
- Method : POST
- URL : /users/login
- Content-Type : application/json
- Authorization : -

<br>

|필드명|타입|필수 여부|설명|
|---|---|:---:|---|
|email|String|Y|이메일|
|password|String|Y|비밀번호|

<br>

```
{
    "email": "user1@naver.com",
    "password": "User123*"
}
```

### Response
|상태 코드|설명|
|---|---|
|200 OK|로그인 성공|
|400 Bad Request|잘못된 요청을 보낼 경우|
|404 Not Found|사용자 이메일이 존재하지 않거나 탈퇴한 사용자인 경우|

<br>

|필드명|타입|설명|
|---|---|---|
|tokenAuthScheme|String|Token Type|
|accessToken|String|Access Token 값|
|refreshToken|String|Refresh Token 값|

<br>

|Header Key|Value|
|---|---|
|Set-Cookie|refreshToken=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBuYXZlci5jb20iLCJpYXQiOjE3NDEyOTQwMDMsImV4cCI6MTc0MTI5NDMwM30.q5XvxJuJDvfxX-vX8IwCLyJTHw92oO0niFvLh-g-bSA; Path=/; Max-Age=300; Expires=Thu, 06 Mar 2025 20:55:23 GMT; Secure; HttpOnly|

<br>

```
{
    "tokenAuthScheme": "Bearer",
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBuYXZlci5jb20iLCJpYXQiOjE3NDEyOTQwMDMsImV4cCI6MTc0MTI5NDE4Mywicm9sZSI6IkFETUlOIn0.8HGYw9GEtaCn0HI7hMTnsQcVkiaq08sLaFX9v-Kqy9U",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBuYXZlci5jb20iLCJpYXQiOjE3NDEyOTQwMDMsImV4cCI6MTc0MTI5NDMwM30.q5XvxJuJDvfxX-vX8IwCLyJTHw92oO0niFvLh-g-bSA"
}
```
```
{
    "timestamp": "2025-03-07T05:48:25.6746263",
    "status": 404,
    "error": "Not Found",
    "code": "EMAIL_NOT_FOUND",
    "message": "해당 이메일이 존재하지 않습니다."
}
```
```
{
    "timestamp": "2025-03-07T05:49:20.1542598",
    "status": 404,
    "error": "Not Found",
    "code": "USER_NOT_FOUND",
    "message": "사용자를 찾을 수 없습니다."
}
```

## 3️⃣ 로그아웃

### Request
- Method : POST
- URL : /users/logout
- Content-Type : -
- Authorization : Access Token

### Response
|상태 코드|설명|
|---|---|
|200 OK|로그아웃 성공|
|401 Unauthorized|Access Token이 만료될 경우|

<br>

|Header Key|Value|
|---|---|
|Set-Cookie|refreshToken=; Path=/; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Secure; HttpOnly|

## 4️⃣ 사용자 단 건 조회

### Request
- Method : GET
- URL : /users/{userId}
- Content-Type : -
- Authorization : Access Token

### Response
|상태 코드|설명|
|---|---|
|200 OK|사용자 단 건 조회 성공|
|401 Unauthorized|Access Token이 만료될 경우|
|404 Not Found|사용자가 존재하지 않을 경우|

<br>

|필드명|타입|설명|
|---|---|---|
|id|Long|사용자 고유 식별자|
|email|String|이메일|
|nickname|String|닉네임|

<br>

```
{
    "id": 1,
    "email": "user1@naver.com",
    "nickname": "jiyeon"
}
```
```
{
    "timestamp": "2025-03-07T06:20:15.7888725",
    "status": 404,
    "error": "Not Found",
    "code": "USER_NOT_FOUND",
    "message": "사용자를 찾을 수 없습니다."
}
```

## 5️⃣ 사용자 전체 조회

### Request
- Method : GET
- URL : /users
- Content-Type : -
- Authorization : Access Token

### Response
|상태 코드|설명|
|---|---|
|200 OK|사용자 전체 조회 성공|
|401 Unauthorized|Access Token이 만료될 경우|

<br>

|필드명|타입|설명|
|---|---|---|
|id|Long|사용자 고유 식별자|
|email|String|이메일|
|nickname|String|닉네임|

<br>

```
[
    {
        "id": 1,
        "email": "user1@naver.com",
        "nickname": "jiyeon"
    },
    {
        "id": 2,
        "email": "user2@naver.com",
        "nickname": "jiyeon"
    }
]
```

## 6️⃣ 회원탈퇴

### Request
- Method : DELETE
- URL : /users/{userId}
- Content-Type : -
- Authorization : Access Token

### Response
|상태 코드|설명|
|---|---|
|204 No Content|회원탈퇴 성공 (응답 본문 없음)|
|401 Unauthorized|Access Token이 만료될 경우|
|404 Not Found|사용자가 존재하지 않을 경우|

<br>

|Header Key|Value|
|---|---|
|Set-Cookie|refreshToken=; Path=/; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Secure; HttpOnly|

<br>

```
{
    "timestamp": "2025-03-07T06:25:23.7202904",
    "status": 404,
    "error": "Not Found",
    "code": "USER_NOT_FOUND",
    "message": "사용자를 찾을 수 없습니다."
}
```

## 7️⃣ 관리자

### Request
- Method : GET
- URL : /admins/access
- Content-Type : -
- Authorization : Access Token

### Response
|상태 코드|설명|
|---|---|
|200 OK|관리자 권한만 접근 허용|
|401 Unauthorized|Access Token이 만료되거나 관리자 권한이 아닐 경우|

<br>

```
관리자만 접근할 수 있습니다.
```

## 8️⃣ Access Token 갱신

### Request
- Method : POST
- URL : /refresh
- Content-Type : -
- Authorization : -
- Cookie

|이름|타입|필수 여부|설명|
|---|---|:---:|---|
|refreshToken|String|N|저장된 Refresh Token 값 (없을 경우 예외 처리)|

### Response
|상태 코드|설명|
|---|---|
|200 OK|Access Token 갱신 성공|
|401 Unauthorized|Refresh Token이 존재하지 않거나, 유효하지 않을 경우|
|404 Not Found|사용자 이메일이 존재하지 않을 경우|

<br>

|필드명|타입|설명|
|---|---|---|
|tokenAuthScheme|String|Token Type|
|accessToken|String|새롭게 발급된 Access Token 값|

<br>

```
{
    "tokenAuthScheme": "Bearer",
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBuYXZlci5jb20iLCJpYXQiOjE3NDEyOTMyMjIsImV4cCI6MTc0MTI5MzQwMiwicm9sZSI6IkFETUlOIn0.EuuG1corMs382Vl5bz1mRiCYIp7ODOOPLAkK7slc8_s"
}
```
```
{
    "timestamp": "2025-03-07T05:41:10.8704737",
    "status": 401,
    "error": "Unauthorized",
    "code": "REFRESH_UNAUTHORIZED",
    "message": "Refresh 토큰이 존재하지 않거나, 유효하지 않습니다."
}
```
```
{
    "timestamp": "2025-03-07T05:42:35.3896119",
    "status": 404,
    "error": "Not Found",
    "code": "EMAIL_NOT_FOUND",
    "message": "해당 이메일이 존재하지 않습니다."
}
```
