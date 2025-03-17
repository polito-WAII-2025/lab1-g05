# Usa l'immagine di base di OpenJDK 17
FROM openjdk:17

# Imposta la directory di lavoro all'interno del container
WORKDIR /app

# Copia tutti i file del progetto nella cartella /app
COPY . /app

# Costruisce il progetto Kotlin con Gradle
RUN ./gradlew build

# Esegue l'applicazione, passando il percorso di `custom-parameters.yml`
CMD ["java", "-jar", "build/libs/RouteAnalyzer-all.jar", "custom-parameters.yml"]

