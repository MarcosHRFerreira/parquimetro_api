
CREATE TABLE Locacao (
    ID_locacao SERIAL not null PRIMARY KEY,
    data_entrada TIMESTAMP NOT NULL,
    data_encerramento TIMESTAMP ,
    ID_condutor BIGINT NOT NULL,
    ID_veiculo BIGINT NOT NULL,
    duracao  BIGINT NOT NULL,
    ID_preco BIGINT NOT NULL,
    valor_cobrado decimal (5,2) not null,
    tipo_pagamento varchar(100) not null,
    status_pagamento varchar(100) not null,
    tipo_periodo varchar(100) not null,
    CONSTRAINT fk_locacao_id_condutor FOREIGN KEY (id_condutor) REFERENCES condutor(id_condutor),
    CONSTRAINT fk_locacao_id_veiculo FOREIGN KEY (id_veiculo) REFERENCES veiculo (id_veiculo)
);

