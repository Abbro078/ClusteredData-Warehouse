package ClusteredDataWarehouse.repository;

import ClusteredDataWarehouse.entity.DealEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<DealEntity, Long> {
}
