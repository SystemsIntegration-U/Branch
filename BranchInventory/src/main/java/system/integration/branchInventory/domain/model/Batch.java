package system.integration.branchInventory.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
import java.time.LocalDate;

@Entity
@Table(name = "batch")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private int stock;
}
