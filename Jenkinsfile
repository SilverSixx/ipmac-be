pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                // Check out code from version control
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                // Example build steps
                sh 'echo "Building the application..."'
                sh 'echo "Build completed successfully!"'
            }
        }
        
        stage('Test') {
            steps {
                // Example test steps
                sh 'echo "Running tests..."'
                sh 'echo "Tests passed successfully!"'
            }
        }
        
        stage('Deploy') {
            steps {
                // Example deployment steps
                sh 'echo "Deploying to staging environment..."'
                sh 'echo "Deployment completed successfully!"'
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Please check the logs for details.'
        }
        always {
            // Clean up steps that should always run
            echo 'Cleaning up workspace...'
        }
    }
}
