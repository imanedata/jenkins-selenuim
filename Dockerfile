# Utiliser l'image de base selenium/standalone-chrome
FROM selenium/standalone-chrome:latest

# Passer à l'utilisateur root pour installer des outils
USER root

# Mettre à jour et installer Java et Maven
RUN apt-get update && \
    apt-get install -y maven openjdk-21-jdk && \
    apt-get clean

# Ajouter un volume pour le ChromeDriver
# Ce volume permet de monter un répertoire local contenant chromeDriver à l'intérieur du conteneur
VOLUME ["/drivers:/usr/local/bin/chromedriver"]

# Revenir à l'utilisateur selenium pour exécuter Selenium en toute sécurité
USER seluser

# Exposer le port WebDriver (si ce n'est pas déjà fait)
EXPOSE 4444

# Point d'entrée par défaut pour exécuter le conteneur Selenium
ENTRYPOINT ["/opt/bin/entry_point.sh"]

