package ClusteredDataWarehouse.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class Deal {

    @NotNull(message = "Deal ID can't be null")
    @Pattern(regexp = "\\+?\\d+", message = "Deal ID must be a non-negative number")
    private String deal_id;

    @Pattern(regexp = "^[A-Z]{3}$", message = "From currency must be exactly 3 uppercase letters")
    @NotNull(message = "From currency can't be null")
    private String from_currency;

    @Pattern(regexp = "^[A-Z]{3}$", message = "To currency must be exactly 3 uppercase letters")
    @NotNull(message = "To currency can't be null")
    private String to_currency;

    @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2}$", message = "Timestamp must be dd-MM-yyyy HH:mm:ss")
    @NotNull(message = "Timestamp cant be null")
    private String deal_timestamp;

    @NotNull(message = "Amount can't be null")
    @DecimalMin(value = "0.1", message = "Amount should be more than 0")
    private BigDecimal deal_amount;

}
