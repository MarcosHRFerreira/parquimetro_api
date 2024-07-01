create table preco(
    id_preco SERIAL not null PRIMARY KEY,
    modalidade varchar(100) not null,
    valor decimal (5,2) not null);