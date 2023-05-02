FROM adoptopenjdk/openjdk17:jre-17.0.0_35_ubuntu
WORKDIR /app
COPY myproject/build/libs/*.jar /app


CMD ["bin/bash"]