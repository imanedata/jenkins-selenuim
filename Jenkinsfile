pipeline {
    agent {
        docker {
            image 'selenium/standalone-chrome'
            args '--privileged -v /var/run/docker.sock:/var/run/docker.sock'  // Permet à Docker d'utiliser le socket de l'hôte
        }
    }
    stages {
        stage('Install Dependencies') {
            steps {
                script {
                    // Installer Maven et Java dans le conteneur
                    sh '''
                    sudo apt-get update && \
                    sudo apt-get install -y maven openjdk-21-jdk
                    '''
                    
                    // Créer le répertoire cache de Selenium pour éviter les problèmes de permission
                    sh 'sudo mkdir -p /home/seluser/.cache/selenium'
                    sh 'sudo chmod -R 777 /home/seluser/.cache/selenium'
                }
            }
        }
        stage('Run Tests') {
            steps {
                script {
                    // Exécuter les tests Maven dans le conteneur
                    sh 'mvn test -D groups="login" '
                    sh 'mvn test -D groups="CartPageTest" '
                    sh 'mvn test -D groups="CheckoutOverviewTest" '
                    sh 'mvn test -D groups="InventoryPageTest" '
                    sh 'mvn test -D groups="ChekoutPageTest" '
                }
            }
        }
    }
    post {
        always {
            echo 'Tests finished. Check the console output for details!'
        }
    }
}
