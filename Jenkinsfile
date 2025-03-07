pipeline {
    agent any
    stages {
        stage('Build selenium-nodejs Image') {
            steps {
                script {
                    // Vérifier si l'image selenium-nodejs existe déjà
                    def imageExists = sh(script: "docker images -q selenium-nodejs", returnStdout: true).trim()
                    if (imageExists == "") {
                        echo 'Image selenium-nodejs non trouvée, construction en cours...'
                        // Construire l'image selenium-nodejs
                        sh "docker build -t selenium-nodejs ."
                    } else {
                        echo 'Image selenium-nodejs trouvée.'
                    }
                }
            }
        }
        stage('Run Tests') {
            steps {
                script {
                    // Exécuter les tests dans le conteneur Docker selenium-nodejs
                    docker.image('selenium-nodejs').inside {
                        sh 'mvn test '  // Lancer les tests Cypress
                    }
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
