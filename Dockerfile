FROM selenium/standalone-chrome:latest

# Passer à l'utilisateur root pour installer des outils
USER root

# Mettre à jour et installer Java et Maven
RUN apt-get update && \
    apt-get install -y maven openjdk-21-jdk && \
    apt-get clean

# Revenir à l'utilisateur selenium
USER seluser

# Exposer le port WebDriver (si ce n'est pas déjà fait)
EXPOSE 4444
