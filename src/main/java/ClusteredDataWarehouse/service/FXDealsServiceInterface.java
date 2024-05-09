package ClusteredDataWarehouse.service;

import ClusteredDataWarehouse.model.Deal;
import ClusteredDataWarehouse.model.Response;

public interface FXDealsServiceInterface {
    Response addDeal(Deal deal);

    Response getAllDeals();

    Response getById(Long id);
}
