# 1. Amazon Corretto 21 기반
FROM amazoncorretto:21

# 2. 타임존 설정 (OS + JVM)
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 3. 빌드 시 JAR 경로를 외부에서 주입 가능하게 설정
ARG JAR_FILE=build/libs/*.jar

# 4. 작업 디렉토리 설정
WORKDIR /app

# 5. JAR 파일 복사 (app.jar로 고정)
COPY ${JAR_FILE} app.jar

# 6. 외부 포트 노출 (문서 목적 + docker run -P 대비)
EXPOSE 8080

# 7. 빌드 시점에 외부에서 받을 변수
ARG ENVIRONMENT=dev

# 8. 빌드 시 받은 값을 컨테이너 내부의 '환경 변수'로 고정
# 스프링 부트가 이 이름을 보고 프로필을 결정합니다.
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]