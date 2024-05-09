package ClusteredDataWarehouse.controller;

import ClusteredDataWarehouse.model.Deal;
import ClusteredDataWarehouse.model.Response;
import ClusteredDataWarehouse.service.FXDealsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class FXDealsController {

    @Autowired
    private FXDealsService fxDealsService;

    @PostMapping("/add")
    public ResponseEntity<Response> addDeal(@Valid @RequestBody Deal deal) {
        Response response = fxDealsService.addDeal(deal);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/getByID/{id}")
    public ResponseEntity<Response> getDealByID(@PathVariable Long id) {
        Response response = fxDealsService.getById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/getAll")
    public ResponseEntity<Response> getAllDeals() {
        Response response = fxDealsService.getAllDeals();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
