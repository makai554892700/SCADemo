FROM java:8
ADD app.jar /app/
ADD provider.yml /config/
EXPOSE 8102
ENTRYPOINT ["java","-Dfile.encoding=utf-8","-Dserver.port=81022","-jar","/app/app.jar"]


