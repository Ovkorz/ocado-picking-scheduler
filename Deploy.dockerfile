FROM amazoncorretto:17-alpine3.17
WORKDIR /app
COPY build/libs/*.jar /app
COPY src/test/resources/advanced-optimize-order-count/* /app/resources

CMD ["sh"]
