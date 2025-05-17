pipeline {
    agent none

    environment {
        EC2_HOST = 'ec2-13-217-207-2.compute-1.amazonaws.com'
        EC2_USER = 'ubuntu'
        JAR_NAME = 'Airline_management_system-0.0.1-SNAPSHOT.jar'
        APP_PORT = '8080'
        MAVEN_REPO = '/tmp/m2repo'
    }

    stages {
        stage('Checkout') {
            agent any
            steps {
                git branch: 'main', url: 'https://github.com/Ankit0431/Airline_Management_System.git'
            }
        }

        stage('Build') {
            agent {
                docker {
                    image 'maven:3.9.5-eclipse-temurin-21'
                    args '-v /var/lib/jenkins/.m2:/root/.m2'
                }
            }
            steps {
                sh 'mvn -Dmaven.repo.local=$MAVEN_REPO clean package spring-boot:repackage -DskipTests'
            }
        }

        stage('Test') {
            agent {
                docker {
                    image 'maven:3.9.5-eclipse-temurin-21'
                    args '-v /var/lib/jenkins/.m2:/root/.m2'
                }
            }
            steps {
                sh 'mvn -Dmaven.repo.local=$MAVEN_REPO test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Deploy to EC2') {
            agent any
            steps {
                sshagent(credentials: ['ec2-key']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} "mkdir -p ~/airline_app"

                        scp -o StrictHostKeyChecking=no target/${JAR_NAME} ${EC2_USER}@${EC2_HOST}:~/airline_app/

                        ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} << 'EOF'
                        lsof -ti:8080 | xargs -r kill -9 || true
                        nohup java -jar ~/airline_app/${JAR_NAME} --server.port=${APP_PORT} &
                        sleep 5
                        curl -f http://localhost:${APP_PORT}/actuator/health || exit 1
                        EOF
                    """
                }
            }
        }
    }

    post {
    success {
        echo 'Pipeline completed successfully!'
    }
    failure {
        echo 'Pipeline Failed!!'
    }
}
}
