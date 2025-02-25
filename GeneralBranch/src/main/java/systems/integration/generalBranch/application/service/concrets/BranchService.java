package systems.integration.generalBranch.application.service.concrets;

import systems.integration.generalBranch.application.service.interfaces.IBranchService;
import systems.integration.generalBranch.domain.model.Branch;
import org.springframework.stereotype.Service;
import systems.integration.generalBranch.domain.repository.interfaces.IBranchRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class BranchService extends GenericService<Branch, UUID> implements IBranchService {

    private final IBranchRepository branchRepository;

    public BranchService(IBranchRepository branchRepository) {
        super(branchRepository);
        this.branchRepository = branchRepository;
    }

    public Optional<Branch> findByGln(Long gln) {
        return branchRepository.findByGln(gln);
    }

    public Branch updateByGln(Long gln, Branch entity) {
        Branch existing = branchRepository.findByGln(gln)
                .orElseThrow(() -> new RuntimeException("Branch not found with GLN: " + gln));
        entity.setId(existing.getId());
        return branchRepository.save(entity);
    }

    public boolean deleteByGln(Long gln) {
        return branchRepository.findByGln(gln)
                .map(branch -> {
                    branchRepository.deleteById(branch.getId());
                    return true;
                }).orElse(false);
    }
}