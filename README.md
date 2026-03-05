# 여행기 기록 시스템(Travel-log)

**Travelog 프로젝트**는 여행의 모든 순간을 기록하고 공유하는 **여행 기록 및 소셜 플랫폼**입니다. 단순한 정보 나열을 넘어, 사용자가 다녀온 장소와 감상을 타임라인 형식으로 보관하고 다른 여행자들과 소통할 수 있는 공간을 지향합니다.

## 🧭 여행기 서비스 사용자 시나리오
### 1. 서비스 접속
- 사용자는 여행기 서비스에 접속한다.
- 메인 페이지에서 다른 사용자의 여행기를 탐색할 수 있다.
- 로그인하지 않은 상태에서는 조회만 가능하다.
### 2. 로그인
- 사용자는 본인의 여행기를 작성하기 위해 로그인한다.
- 이메일/비밀번호 또는 소셜 로그인으로 인증을 완료한다.
### 3. 여행기 작성
- 사용자는 새로운 여행기 작성을 시작한다.
- 여행 제목과 대표 이미지를 설정한다.
- **일자별(Day 단위)** 로 방문한 장소를 순서대로 추가한다.
  - 방문 장소 입력
  - 해당 장소 사진 업로드
  - 장소에 대한 간단한 메모 작성
- 작성된 Day는 순서에 따라 자동 정렬된다.
- 모든 일정을 입력한 후 여행기를 저장하여 게시한다.

### 4. 여행 경로 지도 표시
- 입력된 장소 정보를 기반으로
- 여행 경로가 지도에 시각적으로 표시된다.
- 일자별 이동 흐름을 한눈에 확인할 수 있다.
- 사용자는 자신의 여행 동선을 지도 기반으로 공유할 수 있다.

## 🛠 기술 스택
| 구분                             | 사용 기술                                                           |
| ------------------------------ | --------------------------------------------------------------- |
| **Language / Framwork**       | Java 21, Spring Boot 3.5.10                                                         |
| **Library**                  |  Spring Security, JPA, JWT, Lombok            |
| **Database**                   | MySQL, H2                                                       |
| **Infra**                      | AWS EC2, S3, Docker, Nginx                                      |
| **Build Tool**                 | Gradle                                                          |
| **CI/CD**                      | GitHub Actions                          |
| **Logging**                    | Log4j2, Promtail                                                |
| **Test**                       | JUnit 5, AssertJ, RestAssured, MockMvc, Mockito                 |
| **Document**                   | Swagger, Spring REST Docs                                       |
| **Monitoring / Observability** | Grafana, Prometheus, Loki, Tempo, Node Exporter |
| **Security / Auth**            | JWT                                                      |

### 1. Framework / Library

#### Spring Boot 3.5.3
- 서버 개발에서, 스프링이 제공해주는 기술(DI, AOP, IoC)들을 이용해 비즈니스 로직에 집중하고자한다.
- 스프링은 많은 설정과 구성이 필요하므로, 스프링 부트를 이용해 초기 설정 과정을 최소화하여 빠르게 개발하고자 한다.
#### JPA
- 객체지향 언어인 Java와 JPA를 사용함으로서 SQL이 아니라 객체 기준으로 쿼리를 작성할 수 있다.
- 따라서 관계(Relation) 중심이 아닌, 객체(Object) 중심으로 설계하도록 돕는다.

### 2. Test

#### JUnit5
- JUnit4에 비해 다양한 애노테이션(@BeforeEach, @AfterEach, @Nested 등)을 지원해 더 유연하다.
- 스프링 부트 2.2 버전 이상부터 spring-boot-starter-test 의존성에 JUnit 5가 기본적으로 포함되어 있고 스프링 공식 문서도 모두 JUnit 5 예제를 중심으로 제공되어 테스트 도구로 선택했다.
#### RestAssured
- RestAssured는 실제 클라이언트가 HTTP 요청을 보내는 방식과 유사하게 테스트를 수행할 수 있다.
- 테스트 코드 작성 시 가독성이 높은 BDD 스타일을 사용하기로 팀 내 컨벤션을 정했고 RestAssured는 given-when-then 구조의 BDD 스타일 문법을 지원하기 때문에 HTTP 통합 테스트 도구로 선택했다.
### 3. 문서화

#### Swagger
- 요구사항 빈번하게 변경되는 개발 초기에 빠르게 API 문서에 반영할 수 있다.
- Swagger에서 쉽게 API를 호출할 수 있어서, 프론트와의 협업에 편리하다.

## 🌐 인프라 아키텍처 구조(Backend)

<img width="800" height="500" alt="여행_백엔드_인프라아키텍처" src="https://github.com/user-attachments/assets/7c75f128-707f-49e9-97a9-56775fab4835" />

- **CI/CD 자동화**
  - Github Action을 활용하여 소스 빌드 → Docker 이미지 생성 → 서버 배포까지 자동화 합니다.
- **로그 수집 및 분석 시스템**
  - 서비스의 로그 수집, 저장, 시각화를 위해 Promtail + Loki + Grafana 조합으로 구축합니다.
  - Prometheus API를 이용해 실시간 pull 방식으로 데이터를 가져와 Grafana를 통해 시각화합니다.

## 성능 개선
### Query Optimization
[문제 상황] 여행기 목록 조회시 정렬과 필터링이 포함된 복합 쿼리에서 인덱스를 제대로 활용하지 못해 성능 저하 발생

[해결 과정] 쿼리 실행 계획 분석을 통해 단계적으로 최적화 수행
- 1차: 서브 쿼리로 GROUP BY와 JOIN 순서 변경으로 인덱스 정렬 순서 보장 후 커버링 인덱스
- 2차: 복합 인덱스 확용을 높이기 위해 GROUP BY 제거
- 3차: EXIST를 활용한 SEMI JOIN 활용

[결과]:  **4초 -> 0.5초 -> 0.081초**로 약 50배로 성능 개선(약 98% 실행시간 감소)

[문제 해결 과정 보러가기](https://buly.kr/H6jT9Qo)
