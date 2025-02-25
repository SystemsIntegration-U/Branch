package systems.integration.branch.handler;

import org.springframework.stereotype.Service;
import systems.integration.branch.Interface.IMedicineService;
import systems.integration.branch.Interface.IRequestHandler;
import systems.integration.branch.command.CheckStockQuery;

import java.util.concurrent.CompletableFuture;

@Service
public class CheckStockHandler implements IRequestHandler<CheckStockQuery, Integer> {

    private final IMedicineService medicineService;

    public CheckStockHandler(IMedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @Override
    public CompletableFuture<Integer> handle(CheckStockQuery query) {
        return CompletableFuture.supplyAsync(() -> 
            medicineService.checkStock(query.getMedicineId())
        );
    }
}
