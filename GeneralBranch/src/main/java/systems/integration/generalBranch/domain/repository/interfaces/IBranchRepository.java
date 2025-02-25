package systems.integration.generalBranch.domain.repository.interfaces;

import org.springframework.stereotype.Repository;
import systems.integration.generalBranch.domain.model.Branch;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IBranchRepository extends IRepository<Branch, UUID>{
    Optional<Branch> findByGln(Long gln);
}
