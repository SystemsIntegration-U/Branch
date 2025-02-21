package systems.integration.branch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.integration.branch.domain.Branch;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByName(String name);
    List<Branch> findByLocation(String location);
}
