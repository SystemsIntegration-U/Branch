package systems.integration.branch.handler;

import org.springframework.stereotype.Service;
import systems.integration.branch.Interface.IMedicineService;
import systems.integration.branch.command.CreateMedicineCommand;

import java.util.concurrent.CompletableFuture;

@Service
public class CreateMedicineHandler implements IRequestHandler<CreateMedicineCommand, Medicine> {

    private final IMedicineService medicineService;

    public CreateMedicineHandler(IMedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @Override
    public CompletableFuture<Medicine> handle(CreateMedicineCommand command) {
        return CompletableFuture.supplyAsync(() -> 
            medicineService.createMedicine(command.getMedicineDTO())
        );
    }
}
