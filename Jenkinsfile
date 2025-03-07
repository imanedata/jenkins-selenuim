pipeline {
    agent any
    stages {
        stage('Build selenium-nodejs Image') {
            steps {
                script {
                    // Construire l'image selenium-nodejs si elle n'existe pas
                    def imageExists = sh(script: "docker images -q selenium-nodejs", returnStdout: true).trim()
                    if (imageExists == "") {
                        echo 'Image selenium-nodejs non trouvée, construction en cours...'
                        sh "docker build -t selenium-nodejs ."
                    } else {
                        echo 'Image selenium-nodejs trouvée.'
                    }
                }
            }
        }
        stage('Run Tests') {
            steps {
                // Utilise l'image locale selenium-nodejs
                docker.image('selenium-nodejs').inside {
                    sh 'npm ci'
                    sh 'npx cypress run'
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
