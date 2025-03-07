# Utiliser l'image de base selenium/standalone-chrome
FROM selenium/standalone-chrome:latest

# Passer à l'utilisateur root pour installer des outils
USER root

# Mettre à jour et installer Java et Maven
RUN apt-get update && \
    apt-get install -y maven openjdk-21-jdk && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Ajouter un volume pour le ChromeDriver
# Ce volume permet de monter un répertoire local contenant chromeDriver à l'intérieur du conteneur
VOLUME ["/drivers:/usr/local/bin/chromedriver"]
VOLUME ["/home/seluser/.cache/selenium/chrome/user-data:/home/seluser/.cache/selenium/chrome/user-data"]

# Créer le répertoire de cache pour Selenium et ajuster les permissions
RUN mkdir -p /home/seluser/.cache/selenium && \
    chown -R seluser:seluser /home/seluser/.cache/selenium

# Revenir à l'utilisateur selenium pour exécuter Selenium en toute sécurité
USER seluser

# Exposer le port WebDriver
EXPOSE 4444

# Point d'entrée par défaut pour exécuter le conteneur Selenium (déjà par défaut)
# ENTRYPOINT ["/opt/bin/entry_point.sh"]  # Ce n'est pas nécessaire car c'est déjà configuré dans l'image par défaut
