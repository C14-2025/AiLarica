-- Criação da tabela restaurante
CREATE TABLE IF NOT EXISTS restaurante (
                                           idRestaurante INT AUTO_INCREMENT PRIMARY KEY,
                                           nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    endereco VARCHAR(255),
    telefone VARCHAR(20),
    avaliacao FLOAT DEFAULT 0.0,
    ativo BOOLEAN DEFAULT TRUE,
    fotoPerfil VARCHAR(255)
    );

-- Criação da tabela prato
CREATE TABLE IF NOT EXISTS prato (
                                     idPrato INT AUTO_INCREMENT PRIMARY KEY,
                                     nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10, 2) NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE,
    foto VARCHAR(255),
    idRestaurante INT NOT NULL,
    FOREIGN KEY (idRestaurante) REFERENCES restaurante(idRestaurante) ON DELETE CASCADE
    );

-- Criação da tabela restaurante_horario (para armazenar os horários de funcionamento)
-- Como o modelo RestauranteHorario não foi detalhado, vou criar uma estrutura simples
-- que pode ser expandida depois, se necessário. Por enquanto, vou ignorar, pois o
-- foco é o CRUD principal. O campo 'horarios' em Restaurante.java é um objeto complexo.
-- Para simplificar e focar no CRUD principal, vou ignorar a tabela RestauranteHorario
-- por enquanto, pois o DAO não a implementa e o modelo a trata como um objeto
-- que seria serializado/deserializado, o que não é o foco do JDBC.
