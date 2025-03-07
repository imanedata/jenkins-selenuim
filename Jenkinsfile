pipeline {
    agent {
        docker {
            image "selenium/standalone-chrome"
            args "--network host"
        }
    }
    environment {
        SELENIUM_URL = "http://localhost:4444/wd/hub"
    }
    stages {
        stage("Install dependencies") {
            steps {
                sh "npm ci"
            }
        }
        stage("Run Selenium tests") {
            steps {
                sh """
                echo "Running Selenium tests..."
                npx mocha --timeout 30000 test/specs/*.js
                """
            }
        }
    }
    post {
        always {
            script {
                echo "Tests finished. Collecting results..."
            }
            archiveArtifacts artifacts: '**/test-results/*.xml', allowEmptyArchive: true
            junit '**/test-results/*.xml'
        }
    }
}
