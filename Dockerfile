# Utiliser l'image Selenium Standalone Chrome comme base
FROM selenium/standalone-chrome:latest

# Installer Java et Maven
USER root
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

# Vérifier l'installation de Maven et Java
RUN java -version && \
    mvn -version

# Exposer le port WebDriver
EXPOSE 4444

# Revenir à l'utilisateur selenium pour éviter les problèmes de permission
USER seluser

# Définir le répertoire de travail et copier les fichiers
WORKDIR /app
COPY . /app

# Commande par défaut pour démarrer le conteneur
CMD ["bash"]
