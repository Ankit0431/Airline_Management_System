pipeline {
    agent {
        docker {
            image 'maven:3.9.5-eclipse-temurin-21'
            args '-v /root/.m2:/root/.m2'
        }
    }

    environment {
        EC2_USER = "ubuntu"
        EC2_HOST = "ec2-3-91-214-202.compute-1.amazonaws.com"
        JAR_NAME = "Airline_management_system-0.0.1-SNAPSHOT.jar"
        TARGET_DIR = "airline_app"
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/Ankit0431/Airline_Management_System.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests=false'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Deploy to EC2') {
            steps {
                // Use Jenkins SSH credentials securely
                sshagent(['ec2-key']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} "mkdir -p ~/${TARGET_DIR}"
                    scp -o StrictHostKeyChecking=no target/${JAR_NAME} ${EC2_USER}@${EC2_HOST}:~/${TARGET_DIR}/
                    """
                }
            }
        }

        stage('Start Application on EC2') {
            steps {
                sshagent(['ec2-key']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} << EOF
                        pkill -f '${JAR_NAME}' || true
                        nohup java -jar ~/${TARGET_DIR}/${JAR_NAME} > ~/${TARGET_DIR}/app.log 2>&1 &
                    EOF
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Deployment succeeded!'
        }
        failure {
            echo 'Pipeline failed. Check the logs.'
        }
    }
}
