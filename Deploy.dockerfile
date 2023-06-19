FROM amazoncorretto:17-alpine3.17
WORKDIR /app

RUN set -o errexit -o nounset \
    && apk add --update --no-cache \
    openssh 

COPY build/libs/*.jar /app
COPY src/test/resources/advanced-optimize-order-count/* /app/resources
COPY deploy-entrypoint.sh /entrypoint.sh


RUN adduser --system --uid 1000 -s /bin/sh -h /app scheduler \
    && echo -n 'scheduler:1234' | chpasswd \
    && echo 'PasswordAuthentication yes' >> /etc/ssh/sshd_config \
    && chown -R scheduler /app 

EXPOSE 22

ENTRYPOINT ["/entrypoint.sh"]