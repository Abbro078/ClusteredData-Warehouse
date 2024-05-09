package ClusteredDataWarehouse;

import ClusteredDataWarehouse.entity.DealEntity;
import ClusteredDataWarehouse.exception.DealExistsException;
import ClusteredDataWarehouse.exception.InvalidCurrencyException;
import ClusteredDataWarehouse.exception.NoDealFoundException;
import ClusteredDataWarehouse.model.Deal;
import ClusteredDataWarehouse.model.Response;
import ClusteredDataWarehouse.repository.DealRepository;
import ClusteredDataWarehouse.service.FXDealsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest
class ClusteredDataWarehouseApplicationTests {

    private DealRepository dealRepository;
    private FXDealsService fxDealService;

    @BeforeEach
    public void setup() {
        dealRepository = mock(DealRepository.class);
        fxDealService = new FXDealsService(dealRepository);
    }

    @Test
    void should_return_valid_response_when_getting_deal_by_id() {
        Long id = 1L;
        DealEntity entity = buildEntity(id);
        when(dealRepository.findById(id)).thenReturn(Optional.of(entity));
        Response response = fxDealService.getById(id);
        verify(dealRepository).findById(id);
        assertEquals(OK.value(), response.getStatus());
        assertEquals("Deal with id [" + id + "] is successfully retrieved", response.getMessage());
    }

    @Test
    void should_throw_exception_when_getting_nonexistent_deal_by_id() {
        Long id = 9999L;
        when(dealRepository.findById(id)).thenReturn(Optional.empty());
        NoDealFoundException exception = assertThrows(NoDealFoundException.class, () ->
                fxDealService.getById(id));
        assertEquals("Deal with ID [" + id + "] not found", exception.getMessage());
    }

    @Test
    void should_return_valid_response_when_getting_all_deals() {
        DealEntity entity1 = buildEntity(1L);
        DealEntity entity2 = buildEntity(2L);
        DealEntity entity3 = buildEntity(3L);
        DealEntity entity4 = buildEntity(4L);

        when(dealRepository.findAll()).thenReturn(List.of(entity1, entity2, entity3, entity4));
        Response response = fxDealService.getAllDeals();
        System.out.println(response.getData());

        assertEquals(OK.value(), response.getStatus());
        assertEquals("Deals were retrieved successfully", response.getMessage());
    }

    @Test
    void should_return_no_deals_found_when_getting_all_deals() {
        when(dealRepository.findAll()).thenReturn(List.of());
        Response response = fxDealService.getAllDeals();
        assertEquals(NO_CONTENT.value(), response.getStatus());
        assertEquals("No deals were found", response.getMessage());
    }

    @Test
    void should_successfully_save_deal_when_none_exist_with_given_id() {
        Deal deal = buildModel("5");
        DealEntity entity = buildEntity(5L);
        when(dealRepository.findById(5L)).thenReturn(Optional.empty());
        Response response = fxDealService.addDeal(deal);
        verify(dealRepository).save(entity);
        assertEquals(CREATED.value(), response.getStatus());
        assertEquals("Deal with id [" + entity.getDeal_id() + "] successfully saved", response.getMessage());
    }

    @Test
    void should_throw_exception_when_from_currency_is_invalid() {
        Deal deal = buildModel("3");
        deal.setFrom_currency("invalid");
        DealEntity entity = buildEntity(3L);
        when(dealRepository.findById(entity.getDeal_id())).thenReturn(Optional.empty());
        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                () -> fxDealService.addDeal(deal));
        verify(dealRepository, never()).save(entity);
        assertEquals("Invalid from currency ISO code [" + deal.getFrom_currency() + "]", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_to_currency_is_invalid() {
        Deal deal = buildModel("3");
        deal.setTo_currency("invalid");
        DealEntity entity = buildEntity(3L);
        when(dealRepository.findById(entity.getDeal_id())).thenReturn(Optional.empty());
        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                () -> fxDealService.addDeal(deal));
        verify(dealRepository, never()).save(entity);
        assertEquals("Invalid to currency ISO code [" + deal.getTo_currency() + "]", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_deal_exists() {
        Deal deal = buildModel("3");
        DealEntity entity = buildEntity(3L);
        when(dealRepository.findById(entity.getDeal_id())).thenReturn(Optional.of(entity));
        DealExistsException exception = assertThrows(DealExistsException.class, () ->
                fxDealService.addDeal(deal));
        verify(dealRepository, never()).save(entity);
        assertEquals("Deal with ID [" + entity.getDeal_id() + "] already exists", exception.getMessage());
    }

    private DealEntity buildEntity(Long id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return DealEntity.builder()
                .deal_id(id)
                .from_currency("JOD")
                .to_currency("USD")
                .deal_timestamp(LocalDateTime.parse("22-01-2012 12:12:12", formatter))
                .deal_amount(BigDecimal.TEN)
                .build();
    }

    private Deal buildModel(String id) {
        Deal deal = new Deal();
        deal.setDeal_id(id);
        deal.setDeal_timestamp("22-01-2012 12:12:12");
        deal.setFrom_currency("JOD");
        deal.setTo_currency("USD");
        deal.setDeal_amount(BigDecimal.TEN);
        return deal;
    }
}
