package systems.integration.branch.command;

public class CreateMedicineCommand implements IRequest{
    private MedicineDTO medicineDTO;

    public CreateMedicineCommand(MedicineDTO medicineDTO) {
        this.medicineDTO = medicineDTO;
    }

    public MedicineDTO getMedicineDTO() {
        return medicineDTO;
    }
}
