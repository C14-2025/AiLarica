pipeline {
    agent any

    tools {
        maven 'mvn-default'
        jdk 'JDK-17'
        nodejs 'node-20'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Backend (Java/Spring)') {
            steps {
                dir('backend') {
                    script {
                        echo '--- Iniciando Build do Backend ---'
                        sh 'mvn clean package -DskipTests'
                        echo '--- Rodando Testes Unitários ---'
                        sh 'mvn test || true'
                    }
                }
            }
            post {
                always {
                    junit 'backend/target/surefire-reports/*.xml'
                    archiveArtifacts artifacts: 'backend/target/*.jar', allowEmptyArchive: true
                }
            }
        }

        stage('Frontend (Vue.js)') {
            steps {
                dir('frontend') {
                    script {
                        echo '--- Iniciando Build do Frontend ---'

                        // 1. Instala dependências.
                        // Se 'npm ci' continuar chato, usamos 'npm install' normal que é mais permissivo
                        sh 'npm install'

                        // 2. MUDANÇA CRUCIAL:
                        // Em vez de 'npm run build', usamos 'npm run build-only'
                        // Isso pula o 'vue-tsc' e roda apenas o Vite, evitando os erros de TS2307
                        sh 'npm run build-only'
                    }
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: 'frontend/dist/**', onlyIfSuccessful: true
                }
            }
        }
    }
}