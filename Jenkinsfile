pipeline {
    agent any

    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Browser for non-grid local suites')
        choice(name: 'ENV', choices: ['qa', 'prod'], description: 'Target test environment')
        choice(name: 'SUITE', choices: [
                'testng_vwo_pom.xml',
                'testng_api_tests.xml',
                'testng_docker_grid.xml',
                'testng_final_validation.xml'
        ], description: 'TestNG suite to run')
        booleanParam(name: 'USE_DOCKER_GRID', defaultValue: true, description: 'Start Selenium Grid using Docker Compose')
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '20'))
        timeout(time: 45, unit: 'MINUTES')
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        GRID_URL = 'http://localhost:4444/wd/hub'
        MVN_CMD = 'mvn -B -ntp'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "Suite=${params.SUITE}, Browser=${params.BROWSER}, Env=${params.ENV}, DockerGrid=${params.USE_DOCKER_GRID}"
            }
        }

        stage('Verify Tooling') {
            steps {
                script {
                    if (isUnix()) {
                        sh '${MVN_CMD} -version'
                        sh 'docker --version'
                    } else {
                        bat '%MVN_CMD% -version'
                        bat 'docker --version'
                    }
                }
            }
        }

        stage('Start Selenium Grid') {
            when {
                expression { return params.USE_DOCKER_GRID && params.SUITE == 'testng_docker_grid.xml' }
            }
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            if docker compose version >/dev/null 2>&1; then
                              docker compose -f docker-compose.yml up -d selenium-hub chrome firefox
                            else
                              docker-compose -f docker-compose.yml up -d selenium-hub chrome firefox
                            fi
                        '''
                    } else {
                        bat '''
                            docker compose version >nul 2>nul
                            if %errorlevel%==0 (
                                docker compose -f docker-compose.yml up -d selenium-hub chrome firefox
                            ) else (
                                docker-compose -f docker-compose.yml up -d selenium-hub chrome firefox
                            )
                        '''
                    }
                }
            }
        }

        stage('Wait For Grid') {
            when {
                expression { return params.USE_DOCKER_GRID && params.SUITE == 'testng_docker_grid.xml' }
            }
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            for i in $(seq 1 20); do
                              if curl -fsS http://localhost:4444/wd/hub/status >/dev/null 2>&1; then
                                echo "Selenium Grid is ready"
                                exit 0
                              fi
                              echo "Waiting for Selenium Grid... attempt $i"
                              sleep 3
                            done
                            echo "Selenium Grid did not become ready in time"
                            exit 1
                        '''
                    } else {
                        bat '''
                            @echo off
                            setlocal enabledelayedexpansion
                            set READY=0
                            for /L %%i in (1,1,20) do (
                                powershell -NoProfile -Command "try { $r=Invoke-WebRequest -UseBasicParsing http://localhost:4444/wd/hub/status -TimeoutSec 2; if ($r.StatusCode -eq 200) { exit 0 } else { exit 1 } } catch { exit 1 }"
                                if !errorlevel! equ 0 (
                                    echo Selenium Grid is ready
                                    set READY=1
                                    goto :done
                                )
                                echo Waiting for Selenium Grid... attempt %%i
                                timeout /t 3 /nobreak >nul
                            )
                            :done
                            if !READY! neq 1 (
                                echo Selenium Grid did not become ready in time
                                exit /b 1
                            )
                        '''
                    }
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    if (params.SUITE == 'testng_docker_grid.xml' && !params.USE_DOCKER_GRID) {
                        error('testng_docker_grid.xml requires USE_DOCKER_GRID=true')
                    }

                    if (isUnix()) {
                        sh """
                            ${MVN_CMD} clean test \
                              -Denv=${params.ENV} \
                              -Dbrowser=${params.BROWSER} \
                              -Dgrid.url=${env.GRID_URL} \
                              -Dsurefire.suiteXmlFiles=${params.SUITE}
                        """
                    } else {
                        bat """
                            %MVN_CMD% clean test ^
                              -Denv=${params.ENV} ^
                              -Dbrowser=${params.BROWSER} ^
                              -Dgrid.url=${env.GRID_URL} ^
                              -Dsurefire.suiteXmlFiles=${params.SUITE}
                        """
                    }
                }
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }
    }

    post {
        always {
            script {
                if (params.USE_DOCKER_GRID && params.SUITE == 'testng_docker_grid.xml') {
                    if (isUnix()) {
                        sh '''
                            if docker compose version >/dev/null 2>&1; then
                              docker compose -f docker-compose.yml down
                            else
                              docker-compose -f docker-compose.yml down
                            fi
                        '''
                    } else {
                        bat '''
                            docker compose version >nul 2>nul
                            if %errorlevel%==0 (
                                docker compose -f docker-compose.yml down
                            ) else (
                                docker-compose -f docker-compose.yml down
                            )
                        '''
                    }
                }
            }

            archiveArtifacts artifacts: 'target/surefire-reports/**,target/allure-results/**,target/site/**,screenshots/**', allowEmptyArchive: true
            cleanWs()
            success {
                slackSend(
                        webhookUrl: "${env.SLACK_WEBHOOK_URL}",
                        channel: 'selenium-automation-framework',
                        message: "✅ Build ${env.JOB_NAME} #${env.BUILD_NUMBER} PASSED\nDuration: ${currentBuild.durationString}\n${env.BUILD_URL}",
                        color: 'good'
                )
            }

            failure {
                slackSend(
                        webhookUrl: "${env.SLACK_WEBHOOK_URL}",
                        channel: '#selenium-automation-framework',
                        message: "❌ Build ${env.JOB_NAME} #${env.BUILD_NUMBER} FAILED\nDuration: ${currentBuild.durationString}\n${env.BUILD_URL}",
                        color: 'danger'
                )
            }
        }
    }
}