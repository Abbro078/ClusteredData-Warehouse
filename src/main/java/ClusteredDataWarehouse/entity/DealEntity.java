package ClusteredDataWarehouse.entity;

import ClusteredDataWarehouse.model.Deal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fxdeals")
public class DealEntity {

    @Id
    @Column(name = "deal_id", unique = true, nullable = false)
    private Long deal_id;

    @Column(name = "from_currency", nullable = false)
    private String from_currency;

    @Column(name = "to_currency", nullable = false)
    private String to_currency;

    @Column(name = "deal_timestamp", nullable = false)
    private LocalDateTime deal_timestamp;

    @Column(name = "deal_amount", nullable = false)
    private BigDecimal deal_amount;

    public DealEntity toEntity(Deal deal) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return DealEntity.builder()
                .deal_id(Long.valueOf(deal.getDeal_id()))
                .from_currency(deal.getFrom_currency())
                .to_currency(deal.getTo_currency())
                .deal_timestamp(LocalDateTime.parse(deal.getDeal_timestamp(), formatter))
                .deal_amount(deal.getDeal_amount())
                .build();
    }
}
