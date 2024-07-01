package postech.fiap.com.br.parquimetro_api.domain.locacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocacaoRepository extends JpaRepository<LocacaoEntity, Long> {

    @Query("SELECT l FROM LocacaoEntity l WHERE l.data_encerramento IS NULL")
    List<LocacaoEntity> findAllAbertas();
}
