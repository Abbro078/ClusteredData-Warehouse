package ClusteredDataWarehouse.model;

import lombok.Data;

@Data
public class ResponseError {
    private int errorStatus;
    private String errorDescription;
}
