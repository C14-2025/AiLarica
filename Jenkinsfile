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

                        // 1. Compila e gera o JAR (pula testes aqui para ganhar tempo)
                        // Usa 'sh' pois o servidor Jenkins é Linux
                        sh 'mvn clean package -DskipTests'

                        echo '--- Rodando Testes Unitários ---'

                        // 2. Roda os testes
                        // O "|| true" impede que o pipeline pare se um teste falhar,
                        // permitindo que o relatório de erros seja gerado no passo 'post'.
                        sh 'mvn test || true'
                    }
                }
            }
            post {
                always {
                    // Procura os relatórios XML dentro da pasta backend
                    junit 'backend/target/surefire-reports/*.xml'

                    // Guarda o arquivo .jar gerado
                    archiveArtifacts artifacts: 'backend/target/*.jar', allowEmptyArchive: true
                }
            }
        }

        stage('Frontend (Vue.js)') {
            steps {
                dir('frontend') {
                    script {
                        echo '--- Iniciando Build do Frontend (Linux) ---'
                        // Comandos npm usando 'sh'
                        sh 'npm install'
                        sh 'npm run build'
                    }
                }
            }
        }
    }
}