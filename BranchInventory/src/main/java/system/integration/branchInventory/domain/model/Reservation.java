package system.integration.branchInventory.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private LocalDateTime reservationDate;
    private LocalDateTime expiryDate;
    private String status;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReservationDetail> reservationDetails;  

    public List<ReservationDetail> getReservationDetails() {
        return reservationDetails;
    }

    public void setReservationDetails(List<ReservationDetail> reservationDetails) {
        this.reservationDetails = reservationDetails;
    }
}
