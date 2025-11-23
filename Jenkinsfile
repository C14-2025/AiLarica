pipeline {
    agent any

    // Ferramentas configuradas no Jenkins
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
                        echo '--- Iniciando Build do Backend (Linux) ---'

                        // Compila e gera o JAR
                        sh 'mvn clean package -DskipTests'

                        echo '--- Rodando Testes Unitários ---'
                        // O "|| true" permite gerar relatório mesmo com falha
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

                        // MUDANÇA AQUI:
                        // 1. Garante que o cache não atrapalhe (opcional, mas seguro)
                        sh 'rm -rf node_modules'

                        // 2. Força a instalação de dependências de DESENVOLVIMENTO (--include=dev)
                        // Isso resolve o erro "Cannot find module eslint/pinia/vitest"
                        sh 'npm install --include=dev'

                        // 3. Roda o build
                        sh 'npm run build'
                    }
                }
            }
            post {
                success {
                    // Salva a pasta dist gerada
                    archiveArtifacts artifacts: 'frontend/dist/**', onlyIfSuccessful: true
                }
            }
        }
    }
}