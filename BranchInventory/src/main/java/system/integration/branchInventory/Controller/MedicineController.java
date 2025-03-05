package system.integration.branchInventory.Controller;

import org.springframework.web.bind.annotation.*;
import system.integration.branchInventory.domain.model.Medicine;
import system.integration.branchInventory.service.MedicineService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/medicine")
public class MedicineController {
    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping
    public List<Medicine> getAllMedicines() {
        return medicineService.getAllMedicines();
    }

    @GetMapping("/{id}")
    public Medicine getMedicineById(@PathVariable UUID id) {
        return medicineService.getMedicineById(id);
    }

    @PostMapping
    public Medicine createMedicine(@RequestBody Medicine medicine) {
        return medicineService.saveMedicine(medicine);
    }

    @DeleteMapping("/{atc}")
    public void deleteMedicine(@PathVariable String atc) {
        medicineService.deleteMedicineByAtc(atc);
    }

    @PostMapping("/{atc}/reduce-stock")
    public Medicine reduceStock(@PathVariable String atc, @RequestParam int quantity) {
        return medicineService.reduceStockByAtc(atc, quantity);
    }
}