# Utiliser l'image Selenium Standalone Chrome comme base
FROM selenium/standalone-chrome:latest

# Passer à l'utilisateur root pour installer les dépendances
USER root

# Mettre à jour les paquets, installer Maven et Java (OpenJDK 11)
RUN apt-get update && \
    apt-get install -y maven openjdk-11-jdk curl && \
    apt-get clean

# Vérifier que Maven et Java sont installés correctement
RUN java -version && \
    mvn -version

# Revenir à l'utilisateur Selenium pour éviter les problèmes de permission
USER seluser

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers de l'application/test
COPY . /app

# Exposer le port WebDriver (si nécessaire)
EXPOSE 4444

# Définir la commande par défaut du conteneur (si nécessaire)
CMD ["bash"]
