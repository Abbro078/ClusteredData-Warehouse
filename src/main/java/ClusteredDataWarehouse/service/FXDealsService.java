package ClusteredDataWarehouse.service;

import ClusteredDataWarehouse.entity.DealEntity;
import ClusteredDataWarehouse.exception.DealExistsException;
import ClusteredDataWarehouse.exception.FutureTimestampException;
import ClusteredDataWarehouse.exception.InvalidCurrencyException;
import ClusteredDataWarehouse.exception.NoDealFoundException;
import ClusteredDataWarehouse.model.Deal;
import ClusteredDataWarehouse.model.Response;
import ClusteredDataWarehouse.repository.DealRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
@Validated
@Slf4j
public class FXDealsService implements FXDealsServiceInterface {

    private final DealRepository dealRepository;

    @Autowired
    public FXDealsService(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    @Override
    @Transactional
    public Response addDeal(Deal deal) {
        DealEntity dealEntity = new DealEntity().toEntity(deal);
        Long dealID = dealEntity.getDeal_id();
        String from_currency = dealEntity.getFrom_currency();
        String to_currency = dealEntity.getTo_currency();
        LocalDateTime deal_timestamp = dealEntity.getDeal_timestamp();

        checkIfCurrencyValid(from_currency, to_currency);
        isFutureTimestamp(deal_timestamp);

        log.info("Trying to save Deal with ID [{}]", dealID);
        dealRepository.findById(dealID).ifPresent(e -> {
            throw new DealExistsException("Deal with ID [" + dealID + "] already exists");
        });

        DealEntity savedDeal = dealRepository.save(dealEntity);
        log.info("Deal with ID [{}] successfully saved", dealID);
        return new Response(CREATED.value(), "Deal with id [" + dealID + "] successfully saved", savedDeal);
    }

    @Override
    public Response getById(Long id) {
        log.info("Trying to get deal with ID [{}]", id);
        DealEntity entity = dealRepository.findById(id)
                .orElseThrow(() -> new NoDealFoundException("Deal with ID [" + id + "] not found"));
        log.info("Deal with id [{}] is successfully retrieved", id);
        return new Response(OK.value(), "Deal with id [" + id + "] is successfully retrieved", entity);
    }


    @Override
    public Response getAllDeals() {
        log.info("Trying to get all deals");
        List<DealEntity> entities = dealRepository.findAll();
        if (entities.isEmpty()) {
            log.info("No deals were found");
            return new Response(NO_CONTENT.value(), "No deals were found", entities);
        }
        log.info("Deals were retrieved successfully");
        return new Response(OK.value(), "Deals were retrieved successfully", entities);
    }


    public void checkIfCurrencyValid(String fromCurrency, String toCurrency) {
        log.info("Validating currencies: fromCurrencyISOCode={}, toCurrencyISOCode={}", fromCurrency, toCurrency);
        if (fromCurrency.equals(toCurrency)) {
            throw new InvalidCurrencyException("Currency codes must be different");
        }
        if (!isCurrencyValid(fromCurrency)) {
            throw new InvalidCurrencyException("Invalid from currency ISO code [" + fromCurrency + "]");
        }
        if (!isCurrencyValid(toCurrency)) {
            throw new InvalidCurrencyException("Invalid to currency ISO code [" + toCurrency + "]");
        }
        log.info("Currency validation successful");
    }


    public boolean isCurrencyValid(String currency) {
        try {
            Currency.getInstance(currency);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void isFutureTimestamp(LocalDateTime localDateTime) {
        log.info("Validating future timestamp: localDateTime={}", localDateTime);
        LocalDateTime now = LocalDateTime.now();
        if (localDateTime.isAfter(now)) {
            throw new FutureTimestampException("Deal timestamp can't be in the future");
        }
        log.info("Deal timestamp is present or past");
    }
}
