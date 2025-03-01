package system.integration.branchInventory.service;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationConfirmationEvent {
    private UUID reservationId;
    private UUID medicineId;
    private int quantity;
}
