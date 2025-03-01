package systems.integration.generalBranch.application.service.interfaces;

import systems.integration.generalBranch.domain.model.Branch;

import java.util.Optional;
import java.util.UUID;

public interface IBranchService extends IService<Branch, UUID>{
    Optional<Branch> findByGln(Long gln);
    Branch updateByGln(Long gln, Branch entity);
    boolean deleteByGln(Long gln);
}
