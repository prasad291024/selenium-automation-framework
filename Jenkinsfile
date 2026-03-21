pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }

    environment {
        BROWSER          = "${params.BROWSER ?: 'chrome'}"
        ENV              = "${params.ENV ?: 'qa'}"
        ALLURE_RESULTS   = 'target/allure-results'
        SLACK_CHANNEL    = '#selenium-tests'
    }

    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Browser to run tests on')
        choice(name: 'ENV',     choices: ['qa', 'prod'],                description: 'Target environment')
        choice(name: 'SUITE',   choices: [
            'testng_vwo_pom.xml',
            'testng_api_tests.xml',
            'testng_docker_grid.xml',
            'testng_final_validation.xml'
        ], description: 'TestNG suite to run')
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
        disableConcurrentBuilds()
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
                echo "Branch: ${env.BRANCH_NAME} | Browser: ${BROWSER} | Env: ${ENV}"
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile -q'
            }
        }

        stage('Code Quality') {
            parallel {
                stage('Checkstyle') {
                    steps {
                        sh 'mvn checkstyle:check'
                    }
                }
                stage('Security Scan') {
                    steps {
                        sh 'mvn dependency-check:check'
                    }
                }
            }
        }

        stage('Run Tests') {
            steps {
                sh """
                    mvn test \
                        -Dbrowser=${BROWSER} \
                        -Denv=${ENV} \
                        -Dsurefire.suiteXmlFiles=${params.SUITE ?: 'testng_vwo_pom.xml'}
                """
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Code Coverage') {
            steps {
                sh 'mvn jacoco:report'
                jacoco(
                    execPattern: 'target/jacoco.exec',
                    classPattern: 'target/classes',
                    sourcePattern: 'src/main/java',
                    minimumLineCoverage: '70'
                )
            }
        }

        stage('Generate Allure Report') {
            steps {
                sh 'mvn allure:report'
                allure([
                    includeProperties: true,
                    jdk: '',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }

        stage('Docker Grid Smoke') {
            when {
                branch 'master'
            }
            steps {
                sh 'docker compose config'
                echo 'Docker Compose config is valid ✅'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/surefire-reports/**,target/site/jacoco/**,screenshots/**', allowEmptyArchive: true
            cleanWs()
        }
        success {
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'good',
                message: """✅ *Build Passed* - ${env.JOB_NAME} #${env.BUILD_NUMBER}
Branch: `${env.BRANCH_NAME}` | Browser: `${BROWSER}` | Env: `${ENV}`
<${env.BUILD_URL}|View Build> | <${env.BUILD_URL}allure/|Allure Report>"""
            )
        }
        failure {
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'danger',
                message: """❌ *Build Failed* - ${env.JOB_NAME} #${env.BUILD_NUMBER}
Branch: `${env.BRANCH_NAME}` | Browser: `${BROWSER}` | Env: `${ENV}`
<${env.BUILD_URL}|View Build> | <${env.BUILD_URL}console|Console Log>"""
            )
        }
        unstable {
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'warning',
                message: """⚠️ *Build Unstable* - ${env.JOB_NAME} #${env.BUILD_NUMBER}
Branch: `${env.BRANCH_NAME}` | Browser: `${BROWSER}` | Env: `${ENV}`
<${env.BUILD_URL}|View Build>"""
            )
        }
    }
}
