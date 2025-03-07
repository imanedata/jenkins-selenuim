pipeline {
    agent {
        docker {
            image "cypress/browsers:node20.8.0-chrome118-ff118"
            args "--network host""
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
                echo "Tests finished. Check the console output for details!"
            }
        }
    }
}
