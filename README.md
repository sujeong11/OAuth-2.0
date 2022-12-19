# OAuth-2.0

<br>

구글과 페이스북의 경우에는 Spring oauth에서 공식으로 지원해 기본 설정값을 제공해주기 때문에 client 인증 정보만 입력해주면 되지만, 지원하지 않는 네이버와 카카오톡은 직접 url까지 명시해줘야 한다.

<br>

[1] 구글
> https://console.cloud.google.com/apis/dashboard?hl=ko
<br>

[2] 페이스북
> https://developers.facebook.com/?locale=ko_KR
<br>

[3] 네이버
> https://developers.naver.com/main/
<br>

[4] 카카오톡
> https://developers.kakao.com/

<br>
<br>
<br>

- application.yml 예시

```java
spring:
  application:
    name: oauth2.0

  datasource:
    url: jdbc:mysql://localhost:3306/{DB명}?serverTimezone=UTC&characterEncoding=UTF-8
    username: {사용자이름}
    password: {비밀번호}
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
    open-in-view: true
    show-sql: true
    hibernate:
      format_sql: true
      ddl-auto: update  # none, update, create, create-drop, validate

  security:
    oauth2:
      client:
        registration:
          google:
            client-id: {인증정보}
            client-secret: {인증정보}
            scope:
            - email
            - profile

          facebook:
            client-id: {인증정보}
            client-secret: {인증정보}
            scope:
            - email
            - public_profile

          naver:
            client-id: {인증정보}
            client-secret: {인증정보}
            scope:
            - name
            - email
            client-name: Naver
            authorization-grant-type: authorization_code
            redirect-uri: {원하는값}

        provider:
          naver:
            authorization-uri: https://nid.naver.com/oauth2.0/authorize
            token-uri: https://nid.naver.com/oauth2.0/token
            user-info-uri: https://openapi.naver.com/v1/nid/me
            user-name-attribute: response
```
