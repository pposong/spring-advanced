# 🌱 Spring Advanced

## 📌 프로젝트 소개

Spring Boot 기반의 일정 관리 애플리케이션입니다.
JWT 인증, JPA 연관관계, AOP/Interceptor 로깅 등 다양한 백엔드 핵심 기술을 적용했습니다.

---

## 🛠 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.3.3 |
| ORM | Spring Data JPA / Hibernate |
| Auth | JWT (jjwt 0.11.5) |
| Security | BCrypt (at.favre.lib 0.10.2) |
| DB | MySQL |
| Test | JUnit 5, Mockito |
| Build | Gradle |

---

## ✅ 구현 기능

### Lv 0. 프로젝트 세팅 - 에러 분석
- 애플리케이션 실행 실패 원인을 분석하고 수정하여 정상 실행되도록 해결했습니다.

---

### Lv 1. ArgumentResolver
- `AuthUserArgumentResolver`를 구현하여 `@Auth` 어노테이션이 붙은 파라미터에 JWT 토큰 기반 인증 유저 정보(`AuthUser`)를 자동 주입합니다.
- `WebMvcConfig`에 `ArgumentResolver`를 등록했습니다.

---

### Lv 2. 코드 개선

#### 2-1. Early Return
- `AuthService.signup()` 에서 이메일 중복 체크를 `passwordEncoder.encode()` 호출 이전으로 이동하여, 중복 이메일인 경우 불필요한 암호화 연산이 실행되지 않도록 개선했습니다.

#### 2-2. 불필요한 if-else 제거
- `WeatherClient.getTodayWeather()` 의 중첩 `if-else` 구조를 Early Return 패턴으로 리팩토링하여 가독성을 향상시켰습니다.

#### 2-3. Validation
- `UserChangePasswordRequest` DTO에 `@Pattern` 어노테이션을 적용하여 비밀번호 유효성 검사(8자 이상, 숫자 및 대문자 포함)를 서비스 레이어가 아닌 요청 단계에서 처리하도록 개선했습니다.

---

### Lv 3. N+1 문제 해결
- `TodoRepository.findAllByOrderByModifiedAtDesc()` 에서 JPQL `fetch join` 방식을 `@EntityGraph(attributePaths = "user")` 로 변경하여 N+1 문제를 해결했습니다.

---

### Lv 4. 테스트 코드 연습

| 테스트 | 수정 내용 |
|--------|-----------|
| `PasswordEncoderTest` | `@InjectMocks` → 실제 동작 가능하도록 수정 |
| `ManagerServiceTest` - `manager_목록_조회_시_Todo가_없다면_NPE_에러를_던진다` | 메서드명 및 예외 타입을 `InvalidRequestException`으로 수정 |
| `CommentServiceTest` - `comment_등록_중_할일을_찾지_못해_에러가_발생한다` | Mock 설정 메서드를 `findById()`로 수정 |
| `ManagerServiceTest` - `todo의_user가_null인_경우_예외가_발생한다` | 서비스 로직에 null 체크 추가하여 테스트 통과 |

---

### Lv 5. API 로깅

#### Interceptor (`AdminAccessInterceptor`)
- `/admin/**` 경로 접근 시 요청 시각 및 URL을 로깅합니다.
- 어드민 권한 검증은 `JwtFilter`에서 선처리됩니다.

#### AOP (`AdminApiLoggingAspect`)
- `@Around` 어드바이스를 활용하여 어드민 컨트롤러 메서드 실행 전후로 다음 정보를 로깅합니다.
  - 요청한 사용자 ID
  - API 요청 시각
  - API 요청 URL
  - RequestBody (메서드 파라미터)
  - ResponseBody (메서드 반환값)

---

## 🧪 테스트 커버리지

> 📸 커버리지 스크린샷

<img width="776" height="247" alt="image" src="https://github.com/user-attachments/assets/a746eacb-9a52-4d3a-8108-20a2dff75c37" />


---

## 📂 패키지 구조

```
src/main/java/org/example/expert
├── aop/                  # AOP 로깅
├── client/               # 외부 날씨 API 클라이언트
├── config/               # JWT, Filter, ArgumentResolver, Interceptor 설정
└── domain/
    ├── auth/             # 회원가입 / 로그인
    ├── comment/          # 댓글
    ├── common/           # 공통 DTO, 어노테이션, 예외
    ├── manager/          # 담당자 관리
    ├── todo/             # 일정
    └── user/             # 유저
```

---

## ⚠️ 트러블슈팅

> TIL 블로그 링크:<br>
https://velog.io/@okokok_1/내일배움캠프-Spring-백엔드-DAY-40-심화-Spring-코드-개선-과제
https://velog.io/@okokok_1/내일배움캠프-Spring-백엔드-DAY-41-심화-Spring-코드-개선-과제
