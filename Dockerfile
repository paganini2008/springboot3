FROM openjdk:21-alpine

LABEL maintainer paganini.fy@gmail.com

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms512m -Xmx1024m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /user-management-service

EXPOSE 18083

ADD ./all-modules/user-management-service-1.0.0-SNAPSHOT.jar ./

CMD java $JAVA_OPTS -jar user-management-service-1.0.0-SNAPSHOT.jar
