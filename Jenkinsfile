pipeline {
    agent { docker { image 'selenium/standalone-chrome' } }
    stages {
        stage('Install Dependencies') {
            steps {
                script {
                    // Installer Java et Maven si ce n'est pas déjà fait dans l'image
                    sh 'apt-get update && apt-get install -y maven openjdk-21-jdk'
                }
            }
        }
        stage('Run Tests') {
            steps {
                script {
                    // Exécuter les tests dans le conteneur Docker selenium/standalone-chrome
                    docker.image('selenium/standalone-chrome').inside('--ulimit nofile=32768') {
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
