FROM amazoncorretto:17-alpine3.17
WORKDIR /app

RUN set -o errexit -o nounset \
    && apk add --no-cache \
    openssh-server \
    openrc


COPY build/libs/*.jar /app
COPY src/test/resources/advanced-optimize-order-count/* /app/resources


RUN adduser --system --uid 1000 scheduler \
    && echo 'scheduler:1234' | chpasswd \
    && chown -R scheduler /app

RUN /usr/sbin/sshd \
    && service sshd start 

EXPOSE 22

CMD ["/usr/sbin/sshd","-D"]