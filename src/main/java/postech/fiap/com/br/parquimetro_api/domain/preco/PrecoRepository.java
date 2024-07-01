package postech.fiap.com.br.parquimetro_api.domain.preco;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrecoRepository extends JpaRepository<PrecoEntity,Long> {
}
