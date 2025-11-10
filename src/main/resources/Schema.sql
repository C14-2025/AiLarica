-- Ativar suporte a chaves estrangeiras
PRAGMA foreign_keys = ON;

-- Criação da tabela restaurante
CREATE TABLE IF NOT EXISTS restaurante (
                                           idRestaurante INTEGER PRIMARY KEY AUTOINCREMENT,
                                           nome TEXT NOT NULL,
                                           descricao TEXT,
                                           endereco TEXT,
                                           telefone TEXT,
                                           avaliacao REAL DEFAULT 0.0,
                                           ativo INTEGER DEFAULT 1,  -- 1 = TRUE, 0 = FALSE
                                           fotoPerfil TEXT
);

-- Criação da tabela prato
CREATE TABLE IF NOT EXISTS prato (
                                     idPrato INTEGER PRIMARY KEY AUTOINCREMENT,
                                     nome TEXT NOT NULL,
                                     descricao TEXT,
                                     preco REAL NOT NULL,
                                     isponivel INTEGER DEFAULT 1,  -- 1 = TRUE, 0 = FALSE
                                     foto TEXT,
                                     idRestaurante INTEGER NOT NULL,
                                     FOREIGN KEY (idRestaurante) REFERENCES restaurante(idRestaurante) ON DELETE CASCADE
    );

-- Criação da tabela restaurante_horario (para armazenar os horários de funcionamento)
-- Como o modelo RestauranteHorario não foi detalhado, vou criar uma estrutura simples
-- que pode ser expandida depois, se necessário. Por enquanto, vou ignorar, pois o
-- foco é o CRUD principal. O campo 'horarios' em Restaurante.java é um objeto complexo.
-- Para simplificar e focar no CRUD principal, vou ignorar a tabela RestauranteHorario
-- por enquanto, pois o DAO não a implementa e o modelo a trata como um objeto
-- que seria serializado/deserializado, o que não é o foco do JDBC.
