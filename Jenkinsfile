pipeline {
    agent any

    environment {
        MONGO_HOST = "mongodb"
        MONGO_PORT = "27017"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh 'chmod +x mvnw'
                // Run tests to ensure quality before deployment
                sh './mvnw test'
            }
        }

        stage('Update & Deploy Docker') {
            steps {
                // The --build flag ensures the multi-stage Dockerfile builds the latest .war 
                sh 'docker-compose up -d --build'
            }
        }

        stage('Health Check') {
            steps {
                script {
                    echo "Waiting 10s for MongoDB and Server..."
                    sleep 10
                    sh 'docker ps'
                    sh 'docker logs servicesepa26'
                    sh 'docker logs sepa26-mongodb'
                }
            }
        }
    }

    post {
        failure {
            echo "Pipeline failed. Check Docker logs."
            sh 'docker logs servicesepa26'
        }
    }
}
