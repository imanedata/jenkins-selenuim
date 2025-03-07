pipeline {
    agent any
    stages {
        stage('Build Selenium Image') {
            steps {
                script {
                    // Vérifier si l'image selenium-nodejs existe déjà
                    def imageExists = sh(script: "docker images -q selenium-nodejs", returnStdout: true).trim()
                    if (imageExists == "") {
                        echo 'Image selenium-nodejs non trouvée, construction en cours...'
                        // Construire l'image Selenium avec Maven
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
                    // Exécuter les tests dans l'image Selenium avec l'option --ulimit
                    docker.image('selenium-nodejs').inside('--ulimit nofile=32768') {
                        // Exécuter les tests Maven dans le conteneur
                        sh 'mvn test'  
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
