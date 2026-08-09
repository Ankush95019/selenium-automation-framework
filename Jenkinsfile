pipeline {

    agent any


    // ============================================================
    // TOOLS
    // ============================================================

    tools {

        jdk 'JDK21'

        maven 'Maven'

        nodejs 'NodeJS22'

        allure 'Allure'

    }


    // ============================================================
    // ENVIRONMENT
    // ============================================================

    environment {

        REACT_REPO =
            'https://github.com/Ankush95019/github-actions-demo.git'

        SELENIUM_REPO =
            'https://github.com/Ankush95019/selenium-automation-framework.git'

        REACT_DIR =
            'react-app'

        SELENIUM_DIR =
            'selenium-app'

        ALLURE_REPORT_DIR =
            'allure-report'

    }


    // ============================================================
    // STAGES
    // ============================================================

    stages {


        // ========================================================
        // 1. CLEAN WORKSPACE
        // ========================================================

        stage('Clean Workspace') {

            steps {

                deleteDir()

            }

        }


        // ========================================================
        // 2. CHECKOUT REACT APPLICATION
        // ========================================================

        stage('Checkout React Application') {

            steps {

                dir("${REACT_DIR}") {

                    git(
                        url: "${REACT_REPO}",
                        branch: 'main'
                    )

                }

            }

        }


        // ========================================================
        // 3. VERIFY NODE.JS
        // ========================================================

        stage('Verify Node.js') {

            steps {

                bat '''
                    echo Checking Node.js installation...
                    node --version
                    npm --version
                '''

            }

        }


        // ========================================================
        // 4. INSTALL REACT DEPENDENCIES
        // ========================================================

        stage('Install React Dependencies') {

            steps {

                dir("${REACT_DIR}/frontend") {

                    bat '''
                        echo Installing React dependencies...
                        npm ci
                    '''

                }

            }

        }


        // ========================================================
        // 5. LINT REACT APPLICATION
        // ========================================================

        stage('Lint React Application') {

            steps {

                dir("${REACT_DIR}/frontend") {

                    bat '''
                        echo Running React lint...
                        npm run lint
                    '''

                }

            }

        }


        // ========================================================
        // 6. BUILD REACT APPLICATION
        // ========================================================

        stage('Build React Application') {

            steps {

                dir("${REACT_DIR}/frontend") {

                    bat '''
                        echo Building React application...
                        npm run build
                    '''

                }

            }

        }


        // ========================================================
        // 7. ARCHIVE REACT BUILD
        // ========================================================

        stage('Archive React Build') {

            steps {

                archiveArtifacts(

                    artifacts:
                        "${REACT_DIR}/frontend/dist/**/*",

                    fingerprint: true,

                    allowEmptyArchive: false

                )

            }

        }


        // ========================================================
        // 8. CHECKOUT SELENIUM AUTOMATION REPOSITORY
        // ========================================================

        stage('Checkout Selenium Automation') {

            steps {

                dir("${SELENIUM_DIR}") {

                    git(
                        url: "${SELENIUM_REPO}",
                        branch: 'main'
                    )

                }

            }

        }


        // ========================================================
        // 9. VERIFY JAVA AND MAVEN
        // ========================================================

        stage('Verify Java and Maven') {

            steps {

                bat '''
                    echo Checking Java installation...
                    java -version

                    echo Checking Maven installation...
                    mvn -version
                '''

            }

        }


        // ========================================================
        // 10. START REACT APPLICATION
        // ========================================================

        stage('Start React Application') {

            steps {

                dir("${REACT_DIR}/frontend") {

                    bat '''
                        echo Starting React application...

                        start "React Application" /B npm run start-ci

                        echo React application startup command executed.
                    '''

                }

            }

        }


        // ========================================================
        // 11. WAIT FOR REACT APPLICATION
        // ========================================================

        stage('Wait For React Application') {

            steps {

                bat '''
                    powershell -Command ^
                    "$maxAttempts = 60; ^
                    $url = 'http://localhost:5173'; ^

                    for ($i = 1; $i -le $maxAttempts; $i++) { ^

                        try { ^

                            $response = Invoke-WebRequest ^
                                -Uri $url ^
                                -UseBasicParsing ^
                                -TimeoutSec 2; ^

                            if ($response.StatusCode -eq 200) { ^

                                Write-Host 'React application is running.'; ^

                                exit 0 ^

                            } ^

                        } catch { ^

                            Write-Host 'Application is not ready yet.' ^

                        } ^

                        Write-Host "Waiting for React application... Attempt $i of $maxAttempts"; ^

                        Start-Sleep -Seconds 2 ^

                    }; ^

                    Write-Error 'React application did not start within the expected time.'; ^

                    exit 1"
                '''

            }

        }


        // ========================================================
        // 12. RUN SELENIUM TESTS
        // ========================================================

        stage('Run Selenium Tests') {

            steps {

                dir("${SELENIUM_DIR}") {

                    bat '''
                        echo Running Selenium automation tests...

                        mvn clean test
                    '''

                }

            }

        }


        // ========================================================
        // 13. PUBLISH JUNIT RESULTS
        // ========================================================

        stage('Publish JUnit Results') {

            steps {

                junit(

                    testResults:
                        "${SELENIUM_DIR}/target/surefire-reports/*.xml",

                    allowEmptyResults: true

                )

            }

        }


        // ========================================================
        // 14. GENERATE ALLURE REPORT
        // ========================================================

        stage('Generate Allure Report') {

            steps {

                dir("${SELENIUM_DIR}") {

                    bat '''
                        echo Generating Allure report...

                        if not exist target\\allure-results (

                            echo Allure results directory does not exist.

                            exit /b 1

                        )

                        allure generate ^
                            target\\allure-results ^
                            --clean ^
                            -o ..\\allure-report

                    '''

                }

            }

        }


        // ========================================================
        // 15. PUBLISH ALLURE REPORT INSIDE JENKINS
        // ========================================================

        stage('Publish Allure Report') {

            steps {

                allure(

                    includeProperties: false,

                    jdk: '',

                    results: [

                        [
                            path:
                                "${SELENIUM_DIR}/target/allure-results"
                        ]

                    ]

                )

            }

        }


        // ========================================================
        // 16. ARCHIVE ALLURE REPORT
        // ========================================================

        stage('Archive Allure Report') {

            steps {

                archiveArtifacts(

                    artifacts:
                        "${ALLURE_REPORT_DIR}/**/*",

                    fingerprint: true,

                    allowEmptyArchive: false

                )

            }

        }


        // ========================================================
        // 17. PUBLISH ALLURE TO GITHUB PAGES
        // ========================================================

        stage('Publish Allure To GitHub Pages') {

            steps {

                withCredentials([

                    usernamePassword(

                        credentialsId:
                            'github-pages-credentials',

                        usernameVariable:
                            'GITHUB_USERNAME',

                        passwordVariable:
                            'GITHUB_TOKEN'

                    )

                ]) {

                    bat '''

                        echo Preparing GitHub Pages deployment...

                        if exist pages-deploy (

                            rmdir /S /Q pages-deploy

                        }


                        echo Cloning gh-pages branch...

                        git clone ^
                            --branch gh-pages ^
                            https://%GITHUB_USERNAME%:%GITHUB_TOKEN%@github.com/Ankush95019/selenium-automation-framework.git ^
                            pages-deploy


                        echo Removing previous Allure report...

                        powershell -Command ^
                        "Get-ChildItem -Path 'pages-deploy' -Force | ^
                        Where-Object { $_.Name -ne '.git' } | ^
                        Remove-Item -Recurse -Force"


                        echo Copying new Allure report...

                        xcopy ^
                            "allure-report\\*" ^
                            "pages-deploy\\" ^
                            /E ^
                            /I ^
                            /Y


                        cd pages-deploy


                        echo Configuring Git user...

                        git config user.name "Jenkins"

                        git config user.email "jenkins@localhost"


                        echo Adding Allure report...

                        git add .


                        echo Committing Allure report...

                        git commit ^
                            -m "Update Allure report - Jenkins build %BUILD_NUMBER%" ^
                            || echo No changes to commit.


                        echo Pushing Allure report to GitHub Pages...

                        git push origin gh-pages


                        echo GitHub Pages deployment completed.

                    '''

                }

            }

        }

    }


    // ============================================================
    // POST BUILD ACTIONS
    // ============================================================

    post {


        // ========================================================
        // ALWAYS
        // ========================================================

        always {

            echo """
            
            ============================================
            Jenkins Pipeline Completed
            ============================================

            Job:
            ${env.JOB_NAME}

            Build:
            #${env.BUILD_NUMBER}

            Result:
            ${currentBuild.currentResult}

            Jenkins Build URL:
            ${env.BUILD_URL}

            Allure GitHub Pages URL:
            https://ankush95019.github.io/selenium-automation-framework/

            ============================================
            
            """

        }


        // ========================================================
        // SUCCESS
        // ========================================================

        success {

            echo """

            ============================================
            AUTOMATION SUCCESS
            ============================================

            React build:
            SUCCESS

            Selenium tests:
            SUCCESS

            JUnit:
            PUBLISHED

            Allure:
            GENERATED

            GitHub Pages:
            DEPLOYED

            Report:
            https://ankush95019.github.io/selenium-automation-framework/

            ============================================

            """

        }


        // ========================================================
        // FAILURE
        // ========================================================

        failure {

            echo """

            ============================================
            AUTOMATION FAILED
            ============================================

            Jenkins Job:
            ${env.JOB_NAME}

            Build:
            #${env.BUILD_NUMBER}

            Check Console Output for details.

            Jenkins Build:
            ${env.BUILD_URL}

            ============================================

            """

        }


        // ========================================================
        // UNSTABLE
        // ========================================================

        unstable {

            echo """

            ============================================
            AUTOMATION UNSTABLE
            ============================================

            Some tests may have failed.

            Jenkins Build:
            ${env.BUILD_URL}

            ============================================

            """

        }

    }

}
