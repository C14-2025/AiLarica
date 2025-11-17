-- Ativar suporte a chaves estrangeiras
PRAGMA foreign_keys = ON;

-- Criação da tabela restaurante (CORRIGIDA)
CREATE TABLE IF NOT EXISTS restaurante (
                                           idRestaurante INTEGER PRIMARY KEY AUTOINCREMENT,
                                           nome TEXT NOT NULL,
                                           descricao TEXT,
                                           endereco TEXT,
                                           telefone TEXT,
                                           avaliacao REAL DEFAULT 0.0,
                                           ativo INTEGER DEFAULT 1,
                                           fotoPerfil TEXT,
                                           horarios_json TEXT
);

-- Criação da tabela prato (CORRIGIDA)
CREATE TABLE IF NOT EXISTS prato (
                                     idPrato INTEGER PRIMARY KEY AUTOINCREMENT,
                                     nome TEXT NOT NULL,
                                     descricao TEXT,
                                     preco REAL NOT NULL,
                                     disponivel INTEGER DEFAULT 1,
                                     foto TEXT,
                                     idRestaurante INTEGER NOT NULL,
                                     FOREIGN KEY (idRestaurante) REFERENCES restaurante(idRestaurante) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS usuario (
                                       idUsuario INTEGER PRIMARY KEY AUTOINCREMENT,
                                       nome TEXT NOT NULL,
                                       email TEXT NOT NULL UNIQUE,
                                       senha TEXT NOT NULL,
                                       endereco TEXT,
                                       confirmado INTEGER DEFAULT 0 -- 0 = FALSE, 1 = TRUE
);

-- 1. Tabela "Cabeçalho" do Pedido
CREATE TABLE IF NOT EXISTS pedido (
                                      idPedido INTEGER PRIMARY KEY AUTOINCREMENT,
                                      status TEXT NOT NULL, -- Ex: "PENDENTE", "PREPARANDO", "A CAMINHO", "ENTREGUE"
                                      valorTotal REAL NOT NULL,
                                      dataHora TEXT NOT NULL, -- (SQLite não tem DATETIME, usamos TEXT no formato ISO)

    -- Chaves Estrangeiras
                                      idUsuario INTEGER NOT NULL,
                                      idRestaurante INTEGER NOT NULL,

                                      FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario),
                                      FOREIGN KEY (idRestaurante) REFERENCES restaurante(idRestaurante)
);

-- 2. Tabela "Itens" do Pedido
CREATE TABLE IF NOT EXISTS item_pedido (
                                           idItemPedido INTEGER PRIMARY KEY AUTOINCREMENT,
                                           quantidade INTEGER NOT NULL,
                                           precoNoMomento REAL NOT NULL, -- (Salva o preço do prato no momento da compra)

    -- Chaves Estrangeiras
                                           idPedido INTEGER NOT NULL,
                                           idPrato INTEGER NOT NULL,

                                           FOREIGN KEY (idPedido) REFERENCES pedido(idPedido) ON DELETE CASCADE,
                                           FOREIGN KEY (idPrato) REFERENCES prato(idPrato)
);

-- Criação da tabela restaurante_horario (para armazenar os horários de funcionamento)
-- Como o modelo RestauranteHorario não foi detalhado, vou criar uma estrutura simples
-- que pode ser expandida depois, se necessário. Por enquanto, vou ignorar, pois o
-- foco é o CRUD principal. O campo 'horarios' em Restaurante.java é um objeto complexo.
-- Para simplificar e focar no CRUD principal, vou ignorar a tabela RestauranteHorario
-- por enquanto, pois o DAO não a implementa e o modelo a trata como um objeto
-- que seria serializado/deserializado, o que não é o foco do JDBC.
