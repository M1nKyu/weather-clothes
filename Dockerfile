# Base image 설정
FROM openjdk:17-jdk-slim

# 비대화식 모드로 tzdata 패키지 설치 (debconf를 비대화식으로 설정)
ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y tzdata

# 서울 시간대 설정
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
RUN dpkg-reconfigure --frontend noninteractive tzdata

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY target/weather-clothes-0.0.1-SNAPSHOT.jar weather-clothes.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "weather-clothes.jar"]
