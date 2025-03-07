pipeline {
    agent {
        docker {
            image 'mon-image-selenium'  // Utilise l'image construite avec ton Dockerfile
            args '--privileged -v $(pwd)/drivers:/usr/local/bin/chromedriver'  // Monte le volume
        }
    }
    stages {
        stage('Run Tests') {
            steps {
                script {
                    sh 'mvn test'
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
