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
                }
            }
        }
        stage('Run Tests') {
            steps {
                script {
                    // Passer des options personnalisées à Chrome (spécifier un répertoire de données utilisateur unique)
                    sh 'mvn test -Dchrome.options.args="--user-data-dir=/tmp/chrome-user-data"'
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
