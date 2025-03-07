FROM selenium/standalone-chrome:latest

# Expose le port WebDriver
EXPOSE 4444

# Lancement du serveur Selenium
CMD ["selenium-server", "standalone"]
