pipeline {
    agent any

    tools {
        maven 'Maven 3'
    }

    environment {
        GIT_CREDENTIALS_ID = 'github-pat'
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
        DOCKER_IMAGE = "maxin0525/${params.MICROSERVICE_NAME}"
    }

    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Branch to build')
        booleanParam(name: 'TESTS_EXECUTION', defaultValue: true, description: 'Run tests')
        booleanParam(name: 'BUILD_DOCKER_IMAGE', defaultValue: true, description: 'Build Docker image')
        booleanParam(name: 'UPLOAD_DOCKER_HUB', defaultValue: false, description: 'Push to DockerHub')
        // MICROSERVICE_NAME 是通过 Active Choices Plugin 配置的，不用在这里声明
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Checking out branch: ${params.BRANCH_NAME}"
                    git branch: "${params.BRANCH_NAME}",
                        credentialsId: "${GIT_CREDENTIALS_ID}",
                        url: 'https://github.com/NUS-ISS-SWE/code-connect-backend.git'
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    sh "cd ${params.MICROSERVICE_NAME} && mvn clean verify"
                }
            }
        }

        stage('Test') {
            when {
                expression { return params.TESTS_EXECUTION }
            }
            steps {
                sh "mvn test -pl ${params.MICROSERVICE_NAME}"
            }
        }

        stage('Build Docker Image') {
            when {
                expression { return params.BUILD_DOCKER_IMAGE }
            }
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:latest ./${params.MICROSERVICE_NAME}"
            }
        }

        stage('Upload to DockerHub') {
            when {
                expression { return params.UPLOAD_DOCKER_HUB }
            }
            steps {
                withCredentials([usernamePassword(
                    credentialsId: "${DOCKER_CREDENTIALS_ID}",
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKER_IMAGE}:latest
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Build and Docker steps completed successfully!'
        }
        failure {
            echo '❌ Build failed, please check logs.'
        }
    }
}
