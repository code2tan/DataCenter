# 使用基于Debian的父镜像
FROM openjdk:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制项目的JAR文件到容器中
COPY target/DataCenter-0.0.1-SNAPSHOT.jar app.jar

# 暴露容器的端口（如果需要）
EXPOSE 8080

# 运行Spring Boot应用
ENTRYPOINT ["java", "-jar", "app.jar"]