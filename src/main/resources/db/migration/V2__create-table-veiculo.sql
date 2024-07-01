create table veiculo(
    id_veiculo SERIAL not null PRIMARY KEY,
    modelo varchar(100) not null,
    marca varchar(100) not null,
    placa varchar(20) not null unique,
    tipo_veiculo varchar(50) not null)
