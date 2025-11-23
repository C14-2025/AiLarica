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

                                // MUDANÇA: Usar 'npm ci' é mais seguro e rápido para CI.
                                // Forçamos NODE_ENV=development para garantir que devDependencies sejam baixadas
                                // independentemente da configuração global do Jenkins.
                                sh 'NODE_ENV=development npm ci'

                                // Agora rodamos o build.
                                // O build de produção geralmente define NODE_ENV=production internamente,
                                // mas se precisar forçar, pode fazer:
                                sh 'npm run build'
                            }
                        }
                    }
                    post {
                        success {
                            archiveArtifacts artifacts: 'frontend/dist/**', onlyIfSuccessful: true
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