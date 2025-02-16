pipeline {
    agent any
    environment {
        GIT_CREDENTIALS_ID = 'github-pat'
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
        DOCKER_IMAGE = "your-dockerhub-username/${params.MICROSERVICE_NAME}"
    }

    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Select the branch to build')
        choice(name: 'MICROSERVICE_NAME', choices: [
            'codeconnect-user-management-service',
            'codeconnect-profile-management-service'
        ], description: 'Select the microservice module to build')
        booleanParam(name: 'TESTS_EXECUTION', description: 'Enable or disable test execution')
        booleanParam(name: 'BUILD_DOCKER_IMAGE', description: 'Enable or disable docker build')
        booleanParam(name: 'UPLOAD_DOCKER_HUB', description: 'Enable or disable push to DockerHub')
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
                    echo "Building microservice: ${params.MICROSERVICE_NAME}"
                    sh 'mvn -version'
                    sh "cd ${params.MICROSERVICE_NAME} && mvn clean verify"
                }
            }
        }

        stage('Test & Quality Checks') {
            when {
                expression { return params.RUN_TESTS }
            }
            steps {
                script {
                    echo "Running unit tests for ${params.MICROSERVICE_NAME}"
                    sh "mvn test -pl ${params.MICROSERVICE_NAME}"

                    echo "Running SonarQube analysis"
                    withSonarQubeEnv("${SONARQUBE_SERVER}") {
                        sh "mvn sonar:sonar -Dsonar.projectKey=${params.MICROSERVICE_NAME}"
                    }

                    echo "Running OWASP Dependency Check"
                    sh "mvn org.owasp:dependency-check-maven:check"

                    echo "Running security scan using Trivy"
                    sh "trivy fs --scanners vuln,secret ${params.MICROSERVICE_NAME}"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                when {
                    expression { return params.RUN_TESTS }
                }
                script {
                    echo "Building Docker image for ${params.MICROSERVICE_NAME}"
                    sh "docker build -t ${DOCKER_IMAGE}:latest ./${params.MICROSERVICE_NAME}"
                }
            }
        }

        stage('Upload to DockerHub') {
            steps {
                when {
                    expression { return params.BUILD_DOCKER_IMAGE }
                }
                script {
                    echo "Logging in to DockerHub"
                    sh "echo \$(cat /run/secrets/${DOCKER_CREDENTIALS_ID}) | docker login -u USERNAME --password-stdin"

                    echo "Pushing Docker image to DockerHub"
                    sh "docker push ${DOCKER_IMAGE}:latest"
                }
            }
        }
    }

    post {
        success {
            echo 'Build and upload completed successfully!'
        }
        failure {
            echo 'Build failed! Please check the logs for details.'
        }
    }
}
