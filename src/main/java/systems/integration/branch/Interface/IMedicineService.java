package systems.integration.branch.Interface;

import java.util.UUID;

public interface IMedicineService {
    Medicine createMedicine(MedicineDTO medicineDTO);
    int checkStock(UUID medicineId);
}

