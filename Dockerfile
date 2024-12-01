# Base image 설정
FROM openjdk:17-jdk-slim

# 필수 패키지 설치
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    curl \
    gnupg \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# 비대화식 모드로 tzdata 패키지 설치
ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y tzdata

# 서울 시간대 설정
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime \
    && dpkg-reconfigure --frontend noninteractive tzdata

# 구글 저장소 및 키 추가
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list

# 크롬 및 크롬드라이버 설치
RUN apt-get update \
    && apt-get install -y google-chrome-stable \
    && CHROME_DRIVER_VERSION=$(curl -sS https://chromedriver.storage.googleapis.com/LATEST_RELEASE) \
    && wget -q -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/${CHROME_DRIVER_VERSION}/chromedriver_linux64.zip \
    && unzip /tmp/chromedriver.zip chromedriver -d /usr/local/bin/ \
    && rm /tmp/chromedriver.zip \
    && chmod +x /usr/local/bin/chromedriver

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY target/weather-clothes-0.0.1-SNAPSHOT.jar weather-clothes.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "weather-clothes.jar"]