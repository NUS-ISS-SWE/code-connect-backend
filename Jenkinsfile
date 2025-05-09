pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven 3'
    }

    environment {
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
        GIT_CREDENTIALS_ID = 'github-pat'
        DOCKER_IMAGE = "maxin0525/${params.MICROSERVICE_NAME}"
    }

    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'feature/sprint4/maxin/CDCNT-39-CICD', description: 'Git branch to build')
        choice(name: 'MICROSERVICE_NAME', choices: [
            'codeconnect-user-management-service',
            'codeconnect-profile-management-service'
        ], description: 'Select microservice to build')
        booleanParam(name: 'TESTS_EXECUTION', defaultValue: true, description: 'Run tests and SAST scan')
        booleanParam(name: 'BUILD_DOCKER_IMAGE', defaultValue: true, description: 'Build Docker image')
        booleanParam(name: 'UPLOAD_DOCKER_HUB', defaultValue: true, description: 'Push to DockerHub')
    }

    stages {
        stage('Checkout Source') {
            steps {
                dir('source') {
                    git branch: "${params.BRANCH_NAME}",
                        credentialsId: "${GIT_CREDENTIALS_ID}",
                        url: 'https://github.com/NUS-ISS-SWE/code-connect-backend.git'
                }
            }
        }

        stage('Build') {
            steps {
                dir('source') {
                    sh "cd ${params.MICROSERVICE_NAME} && mvn clean verify"
                }
            }
        }

        stage('Run Unit Tests') {
            when {
                expression { return params.TESTS_EXECUTION }
            }
            steps {
                dir("source/${params.MICROSERVICE_NAME}") {
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit "source/${params.MICROSERVICE_NAME}/target/surefire-reports/*.xml"
                }
            }
        }

        stage('Static Security Scan (SAST & SCA)') {
            when {
                expression { return params.TESTS_EXECUTION }
            }
            steps {
                dir("source/${params.MICROSERVICE_NAME}") {
                    script {
                        def reportDir = "${env.WORKSPACE}/sast-reports"
                        sh """
                            echo '🔐 OWASP Dependency Check (SCA)'
                            mvn org.owasp:dependency-check-maven:check -Dformat=ALL -DoutputDirectory=target
                            mkdir -p ${reportDir}
                            cp target/dependency-check-report.html ${reportDir}/depcheck.html || true
                            cp target/dependency-check-report.json ${reportDir}/depcheck.json || true

                            echo '🔍 Trivy Secret Scan (SAST)'
                            curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -
                            curl -o trivy-html.tpl https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/html.tpl
                            ./bin/trivy fs --scanners secret --format template --template @trivy-html.tpl -o ${reportDir}/trivy-sast.html .
                            ./bin/trivy fs --scanners secret --format json -o ${reportDir}/trivy-sast.json .

                            echo '📁 sast-reports content:'
                            ls -al ${reportDir}
                        """
                    }
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'sast-reports/*.html,sast-reports/*.json', fingerprint: true
                    publishHTML([
                        allowMissing: true,
                        keepAll: true,
                        alwaysLinkToLastBuild: true,
                        reportDir: 'sast-reports',
                        reportFiles: 'depcheck.html',
                        reportName: 'DepCheck SCA'
                    ])
                    publishHTML([
                        allowMissing: true,
                        keepAll: true,
                        alwaysLinkToLastBuild: true,
                        reportDir: 'sast-reports',
                        reportFiles: 'trivy-sast.html',
                        reportName: 'Trivy SAST'
                    ])
                }
            }
        }

        stage('Start Backend Container (for DAST)') {
            steps {
                sh '''
                    docker stop codeconnect-backend || true
                    docker rm codeconnect-backend || true
                    docker run -d --name codeconnect-backend -p 8088:8080 maxin0525/codeconnect-user-management-service:latest
                    sleep 10
                '''
            }
        }

        stage('Dynamic Security Scan (DAST)') {
            steps {
                echo '🧪 Running OWASP ZAP scan...'
                sh '''
                    mkdir -p zap-output
                    docker run -t --rm \
                      -v $(pwd)/zap-output:/zap/wrk/:rw \
                      ghcr.io/zaproxy/zaproxy:stable \
                      zap-baseline.py -t http://172.17.0.1:8088 -r dast-report.html || true
                '''
            }
            post {
                always {
                    script {
                        if (fileExists('zap-output/dast-report.html')) {
                            archiveArtifacts artifacts: 'zap-output/dast-report.html', fingerprint: true
                            publishHTML([
                                allowMissing: true,
                                keepAll: true,
                                alwaysLinkToLastBuild: true,
                                reportDir: 'zap-output',
                                reportFiles: 'dast-report.html',
                                reportName: 'DAST Report'
                            ])
                        } else {
                            echo "❗ No dast-report.html generated, skipping archive/publish."
                        }
                    }
                }
            }
        }

        stage('Build Docker Image') {
            when {
                expression { return params.BUILD_DOCKER_IMAGE }
            }
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:latest ./source/${params.MICROSERVICE_NAME}"
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
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKER_IMAGE}:latest
                    '''
                }
            }
        }
    }

    post {
        success {
            echo '✅ Backend pipeline complete!'
        }
        failure {
            echo '❌ Backend pipeline failed.'
        }
    }
}
