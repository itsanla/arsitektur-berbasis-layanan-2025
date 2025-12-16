pipeline {
    agent any
    
    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials')
        DOCKER_REGISTRY = 'itsanla'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Test') {
            parallel {
                stage('Test Perpustakaan Services') {
                    steps {
                        script {
                            def services = ['Buku', 'anggota', 'Peminjaman', 'Pengembalian', 'api-gateway']
                            services.each { service ->
                                dir("Perpustakaan/${service}") {
                                    sh './mvnw clean test'
                                }
                            }
                        }
                    }
                }
                stage('Test Marketplace Services') {
                    steps {
                        script {
                            def services = ['Produk', 'Pelanggan', 'Order', 'api-gateway']
                            services.each { service ->
                                dir("Marketplace/${service}") {
                                    sh './mvnw clean test'
                                }
                            }
                        }
                    }
                }
            }
        }
        
        stage('Build') {
            steps {
                sh './build-all.sh'
            }
        }
        
        stage('Docker Build') {
            steps {
                sh './build.sh'
            }
        }
        
        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', 
                                               usernameVariable: 'DOCKER_USER', 
                                               passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh './push.sh'
                }
            }
        }
        
        stage('Integration Test') {
            steps {
                sh 'docker-compose -f docker-compose.yaml up -d'
                sh 'sleep 60'
                sh './test-integration.sh'
                sh 'docker-compose down'
            }
        }
    }
    
    post {
        always {
            publishTestResults testResultsPattern: '**/target/surefire-reports/*.xml'
            cleanWs()
        }
    }
}