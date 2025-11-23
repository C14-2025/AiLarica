pipeline {
    agent any

    tools {
        maven 'mvn-default'
        jdk 'JDK-17'
        // Se não tiver o plugin NodeJS instalado, comente a linha abaixo e remova o stage de frontend por enquanto
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
                        // ATENÇÃO: Usando 'bat' porque seu ambiente é Windows.
                        // Se fosse Linux, seria 'sh'.

                        echo '--- Validando e Compilando ---'
                        bat 'mvn clean package -DskipTests'

                        echo '--- Rodando Testes ---'
                        // O "|| exit 0" impede que o pipeline pare se um teste falhar,
                        // permitindo que o Junit processe o relatório depois.
                        bat 'mvn test || exit 0'
                    }
                }
            }
            post {
                always {
                    // O caminho aqui deve ser a partir da RAIZ do projeto
                    junit 'backend/target/surefire-reports/*.xml'

                    archiveArtifacts artifacts: 'backend/target/*.jar', allowEmptyArchive: true
                }
            }
        }

        stage('Frontend (Vue.js)') {
            steps {
                dir('frontend') {
                    // 'bat' para Windows
                    bat 'npm install'
                    bat 'npm run build'
                }
            }
        }
    }
}