FROM java:8
ADD app.jar /app/
ADD gateway.yml /config/
EXPOSE 8100
ENTRYPOINT ["java","-Dfile.encoding=utf-8","-Dserver.port=8100","-jar","/app/app.jar"]


