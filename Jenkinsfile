pipeline {
    agent any

    // Define as ferramentas que configuramos no "Global Tool Configuration"
    tools {
        maven 'mvn-default'   // Nome configurado no passo anterior
        jdk 'JDK-17'      // Seu projeto usa Java 17, não 23
        nodejs 'node-20'  // Necessário para o frontend (Plugin NodeJS)
    }

    stages {
        stage('Checkout') {
            steps {
                // Baixa o código do seu repositório
                checkout scm
            }
        }

        stage('Backend (Java/Spring)') {
            steps {
                dir('backend') { // Entra na pasta do backend
                    script {
                        // 1. Valida estrutura do POM
                        sh 'mvn validate'

                        // 2. Verifica estilo de código (Checkstyle)
                        // O "|| true" impede que o build falhe apenas por estilo, igual ao seu GitHub Actions
                        sh 'mvn -B checkstyle:check || true'

                        // 3. Verifica dependências desatualizadas e salva na raiz
                        sh 'mvn versions:display-dependency-updates > ../outdated.txt || true'

                        // 4. Roda os testes unitários
                        sh 'mvn test'

                        // 5. Gera o artefato final (JAR)
                        sh 'mvn clean package -DskipTests' 
                    }
                }
            }
            post {
                always {
                    // Arquiva os resultados para você ver na interface do Jenkins
                    archiveArtifacts artifacts: 'backend/target/*.jar, backend/target/surefire-reports/**, backend/target/checkstyle-result.xml, outdated.txt', allowEmptyArchive: true, onlyIfSuccessful: false
                    
                    // Opcional: Se tiver o plugin "Junit", exibe gráficos de teste
                    junit 'backend/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Frontend (Vue.js)') {
            steps {
                dir('frontend') { // Entra na pasta do frontend
                    // O plugin NodeJS garante que o 'npm' esteja no PATH aqui
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
            post {
                success {
                    // Arquiva a pasta 'dist' gerada pelo build do Vue
                    archiveArtifacts artifacts: 'frontend/dist/**', onlyIfSuccessful: true
                }
            }
        }
    }
}