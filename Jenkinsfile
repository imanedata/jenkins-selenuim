pipeline {
    agent {
        docker {
            image 'selenium-nodejs'  // Utilise l'image construite localement
            args '--network host'
        }
    }
    stages {
        stage('Install dependencies') {
            steps {
                sh 'npm ci'
            }
        }
        stage('Run Selenium tests') {
            steps {
                sh 'npx cypress run'
            }
        }
    }
    post {
        always {
            echo 'Tests finished. Check the console output for details!'
        }
    }
}
