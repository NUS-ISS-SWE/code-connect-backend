pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
        GIT_CREDENTIALS_ID = 'github-pat'
        DOCKER_IMAGE = "maxin0525/${params.MICROSERVICE_NAME}"
    }

    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Git branch to build')
        choice(name: 'MICROSERVICE_NAME', choices: [
            'codeconnect-user-management-service',
            'codeconnect-profile-management-service'
        ], description: 'Select microservice to build')
        booleanParam(name: 'TESTS_EXECUTION', defaultValue: true, description: 'Run tests and SAST scan')
        booleanParam(name: 'BUILD_DOCKER_IMAGE', defaultValue: true, description: 'Build Docker image')
        booleanParam(name: 'UPLOAD_DOCKER_HUB', defaultValue: false, description: 'Push to DockerHub')
    }

    stages {
        stage('Checkout Source') {
            steps {
                git branch: "${params.BRANCH_NAME}",
                    credentialsId: "${GIT_CREDENTIALS_ID}",
                    url: 'https://github.com/NUS-ISS-SWE/code-connect-backend.git'
            }
        }

        stage('Build') {
            steps {
                sh "cd ${params.MICROSERVICE_NAME} && mvn clean verify"
            }
        }

        stage('Static Security Scan (SAST)') {
            when {
                expression { return params.TESTS_EXECUTION }
            }
            steps {
                script {
                    echo '🔒 OWASP Dependency Check'
                    sh "mvn org.owasp:dependency-check-maven:check -pl ${params.MICROSERVICE_NAME} || true"

                    echo '🕵️ Trivy Secret Scan'
                    sh """
                        curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -
                        ./trivy fs --scanners secret --exit-code 0 --quiet ${params.MICROSERVICE_NAME} || true
                    """
                }
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
            echo '✅ Build, scan and deployment succeeded!'
        }
        failure {
            echo '❌ Pipeline failed.'
        }
    }
}
