//ROLLBACK FILE TO SPECIFIED DATE AND TIME

pipeline {
    agent any

    environment {
        DB_URL = 'jdbc:sqlserver://ip-address;databaseName=;encrypt=true;trustServerCertificate=true'
        DB_USERNAME = 'username'
        DB_PASSWORD = 'password'
        LIQUIBASE_HOME = 'C:\\Program Files\\liquibase'
        CHANGELOG_PATH = 'C:\\ProgramData\\Jenkins\\.jenkins\\changelog-main.xml'
        LIQUIBASE_LICENSE_KEY = 'your_license_key_here'
    }

    stages {
        stage('Check Changelog Location') {
            steps {
                script {
                    echo 'Checking if changelog file exists...'
                    bat 'dir "C:\\ProgramData\\Jenkins\\.jenkins\\changelog-main.xml"'
                    bat 'type "C:\\ProgramData\\Jenkins\\.jenkins\\changelog-main.xml"'
                }
            }
        }

        stage('Liquibase Update') {
            steps {
                script {
                    echo 'Running Liquibase update...'
                    bat """
                    cd /d C:\\ProgramData\\Jenkins\\.jenkins\\
                    \"${LIQUIBASE_HOME}\\liquibase.bat\" ^
                    --url=\"${DB_URL}\" ^
                    --username=\"${DB_USERNAME}\" ^
                    --password=\"${DB_PASSWORD}\" ^
                    --changeLogFile=\"changelog-main.xml\" ^
                    --liquibaseProLicenseKey=\"${LIQUIBASE_LICENSE_KEY}\" ^
                    update
                    """
                }
            }
        }

        stage('Liquibase Rollback') {
            steps {
                script {
                    echo 'Performing Liquibase rollback...'
                    bat """
                    cd /d C:\\ProgramData\\Jenkins\\.jenkins\\
                    \"${LIQUIBASE_HOME}\\liquibase.bat\" ^
                    --url=\"${DB_URL}\" ^
                    --username=\"${DB_USERNAME}\" ^
                    --password=\"${DB_PASSWORD}\" ^
                    --changeLogFile=\"changelog-main.xml\" ^
                    --liquibaseProLicenseKey=\"${LIQUIBASE_LICENSE_KEY}\" ^
                    rollbackToDate 2025-03-06T10:00:00
                    """
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up after pipeline...'
        }
    }
}