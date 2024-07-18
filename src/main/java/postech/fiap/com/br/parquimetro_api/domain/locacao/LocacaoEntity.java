package postech.fiap.com.br.parquimetro_api.domain.locacao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import postech.fiap.com.br.parquimetro_api.domain.condutor.CondutorEntity;
import postech.fiap.com.br.parquimetro_api.domain.preco.PrecoEntity;
import postech.fiap.com.br.parquimetro_api.domain.veiculo.VeiculoEntity;

import java.time.LocalDateTime;

@Table(name="locacao")
@Entity(name="LocacaoEntity")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class LocacaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_locacao;

       private LocalDateTime data_entrada;

    private LocalDateTime data_encerramento;

    private Long duracao;

    @Enumerated(EnumType.STRING)
    private Tipo_Periodo tipo_periodo;

    private Double valor_cobrado;


    public Tipo_Periodo getTipo_periodo() {
        return tipo_periodo;
    }

    public void setTipo_periodo(Tipo_Periodo tipo_periodo) {
        this.tipo_periodo = tipo_periodo;
    }

    private Tipo_Pagamento tipo_pagamento;

    private Status_Pagamento status_pagamento;

    @ManyToOne
    @JoinColumn(name = "id_condutor", referencedColumnName = "id_condutor")
    private CondutorEntity condutorEntity;

    @ManyToOne
    @JoinColumn(name = "id_veiculo", referencedColumnName = "id_veiculo")
    private VeiculoEntity veiculoEntity;

    @OneToOne
    @JoinColumn(name = "id_preco", referencedColumnName = "id_preco")
    private PrecoEntity precoEntity;

    public PrecoEntity getPrecoEntity() {
        return precoEntity;
    }

    public void setPrecoEntity(PrecoEntity precoEntity) {
        this.precoEntity = precoEntity;
    }

    @Override
    public String toString() {
        return "LocacaoEntity{" +
                "id_locacao=" + id_locacao +
                ", data_entrada=" + data_entrada +
                ", data_encerramento=" + data_encerramento +
                ", duracao=" + duracao +
                ", valor_cobrado=" + valor_cobrado +
                ", tipo_pagamento=" + tipo_pagamento +
                ", status_pagamento=" + status_pagamento +
                ", condutorEntity=" + condutorEntity +
                ", veiculoEntity=" + veiculoEntity +
                ", tipo_periodo=" + tipo_periodo +
                '}';
    }

    public Long getId_locacao() {
        return id_locacao;
    }

    public void setId_locacao(Long id_locacao) {
        this.id_locacao = id_locacao;
    }

    public LocalDateTime getData_entrada() {
        return data_entrada;
    }

    public void setData_entrada(LocalDateTime data_entrada) {
        this.data_entrada = data_entrada;
    }

    public LocalDateTime getData_encerramento() {
        return data_encerramento;
    }

    public void setData_encerramento(LocalDateTime data_encerramento) {
        this.data_encerramento = data_encerramento;
    }

    public Long getDuracao() {
        return duracao;
    }

    public void setDuracao(Long duracao) {
        this.duracao = duracao;
    }

    public Double getValor_cobrado() {
        return valor_cobrado;
    }

    public void setValor_cobrado(Double valor_cobrado) {
        this.valor_cobrado = valor_cobrado;
    }

    public Tipo_Pagamento getTipo_pagamento() {
        return tipo_pagamento;
    }

    public void setTipo_pagamento(Tipo_Pagamento tipo_pagamento) {
        this.tipo_pagamento = tipo_pagamento;
    }

    public Status_Pagamento getStatus_pagamento() {
        return status_pagamento;
    }

    public void setStatus_pagamento(Status_Pagamento status_pagamento) {
        this.status_pagamento = status_pagamento;
    }

    public CondutorEntity getCondutorEntity() {
        return condutorEntity;
    }

    public void setCondutorEntity(CondutorEntity condutorEntity) {
        this.condutorEntity = condutorEntity;
    }

    public VeiculoEntity getVeiculoEntity() {
        return veiculoEntity;
    }

    public void setVeiculoEntity(VeiculoEntity veiculoEntity) {
        this.veiculoEntity = veiculoEntity;
    }
}
