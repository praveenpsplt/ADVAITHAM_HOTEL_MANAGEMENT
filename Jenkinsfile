pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        IMAGE_NAME = 'hotel-management-system'
        EC2_APP_DIR = '/opt/hotel-management-system'
        APP_CONTAINER = 'hotel-management-app'
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn -B clean verify'
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_TOKEN')]) {
                    sh '''#!/bin/sh
                        set -eu
                        echo "$DOCKERHUB_TOKEN" | docker login -u "$DOCKERHUB_USER" --password-stdin
                        docker build -t "$DOCKERHUB_USER/$IMAGE_NAME:$BUILD_NUMBER" -t "$DOCKERHUB_USER/$IMAGE_NAME:latest" .
                        docker push "$DOCKERHUB_USER/$IMAGE_NAME:$BUILD_NUMBER"
                        docker push "$DOCKERHUB_USER/$IMAGE_NAME:latest"
                        docker logout
                    '''
                }
            }
        }

        stage('Deploy to AWS EC2') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_TOKEN'), string(credentialsId: 'ec2-host', variable: 'EC2_HOST'), string(credentialsId: 'ec2-ssh-user', variable: 'EC2_SSH_USER')]) {
                    sshagent(credentials: ['ec2-deploy-ssh-key']) {
                        sh '''#!/bin/sh
                            set -eu
                            ssh -o StrictHostKeyChecking=accept-new "$EC2_SSH_USER@$EC2_HOST" \
                              "mkdir -p '$EC2_APP_DIR' && docker pull '$DOCKERHUB_USER/$IMAGE_NAME:$BUILD_NUMBER' && (docker rm -f '$APP_CONTAINER' 2>/dev/null || true)"
                            ssh "$EC2_SSH_USER@$EC2_HOST" \
                              "docker run -d --name '$APP_CONTAINER' --restart unless-stopped --env-file '$EC2_APP_DIR/.env' -p 8080:8080 '$DOCKERHUB_USER/$IMAGE_NAME:$BUILD_NUMBER'"
                        '''
                    }
                }
            }
        }
    }

    post {
        always { archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true }
        success { echo 'Build and deployment pipeline completed.' }
        failure { echo 'Pipeline failed. Review the stage logs before retrying.' }
    }
}
