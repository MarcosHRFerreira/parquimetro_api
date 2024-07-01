package postech.fiap.com.br.parquimetro_api.domain.condutor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CondutorRepository extends JpaRepository<CondutorEntity, Long> {
}
