FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace
COPY . .
RUN ./gradlew :app:installDist --no-daemon

FROM eclipse-temurin:25-jre
RUN useradd --create-home --uid 10001 appuser
WORKDIR /app
COPY --from=build /workspace/app/build/install/app/ /app/
USER appuser
EXPOSE 8081
ENV APP_PORT=8081
ENV APP_HOST=0.0.0.0
CMD ["/app/bin/app"]
