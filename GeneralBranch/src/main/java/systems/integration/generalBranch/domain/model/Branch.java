package systems.integration.generalBranch.domain.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Data
@Entity
@Table(name = "branch")
public class Branch implements IEntityBase {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "gln", nullable = false, unique = true)
    private Long gln;

    private String name;

    @Embedded
    private Location location;

    @Column(name = "port", nullable = false)
    private int port;
}
