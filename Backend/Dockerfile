#Qual Imagem Vou Usar
FROM openjdk:17-jdk-slim
#Apontando o argumento JAR-FILE que requer que eu aponte aonde está localizado a Jar do meu Programa
ARG JAR_FILE=target/*.jar
#Aqui Vai Copiar o meu Jar especificado no JAR_FILE e copiar para app.jar
COPY ${JAR_FILE} app.jar
#São os comandos que vao ser executados assim que o container for executado - este comando abaixo executara o app.jar
ENTRYPOINT ["java","-jar","/app.jar"]