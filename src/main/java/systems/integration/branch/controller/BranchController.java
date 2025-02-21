package systems.integration.branch.controller;

import org.springframework.web.bind.annotation.*;
import systems.integration.branch.domain.Branch;
import systems.integration.branch.service.BranchService;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping
    public List<Branch> getAllBranches() {
        return branchService.getAllBranches();
    }

    @GetMapping("/{id}")
    public Branch getBranchById(@PathVariable Long id) {
        return branchService.getBranchById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
    }

    @PostMapping
    public Branch createBranch(@RequestBody Branch branch) {
        return branchService.createBranch(branch);
    }

    @DeleteMapping("/{id}")
    public void deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
    }
}
