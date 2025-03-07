FROM selenium/standalone-chrome:latest

# Installer Node.js et npm
USER root
RUN apt-get update && \
    apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean

# Vérifier l'installation
RUN node -v && npm -v

# Exposer le port WebDriver (si ce n'est pas déjà fait)
EXPOSE 4444

# Revenir à l'utilisateur selenium pour éviter d'avoir des problèmes de permission
USER selenium
