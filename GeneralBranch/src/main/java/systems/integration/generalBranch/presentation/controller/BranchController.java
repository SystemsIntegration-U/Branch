package systems.integration.generalBranch.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import systems.integration.generalBranch.application.service.interfaces.IBranchService;
import systems.integration.generalBranch.domain.model.Branch;
import systems.integration.generalBranch.infraestructure.mapper.BranchMapper;
import systems.integration.generalBranch.presentation.DTOs.request.BranchDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
public class BranchController {

    private final IBranchService branchService;
    private final BranchMapper branchMapper;

    @GetMapping
    public List<BranchDTO> getBranches() {
        return branchService.findAll().stream()
                .map(branchMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public BranchDTO createBranch(@RequestBody BranchDTO branchDTO) {
        Branch branch = branchMapper.convertToEntity(branchDTO);
        Branch saved = branchService.save(branch);
        return branchMapper.convertToDTO(saved);
    }

    @GetMapping("/{gln}")
    public BranchDTO getBranch(@PathVariable Long gln) {
        Branch branch = branchService.findByGln(gln)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        return branchMapper.convertToDTO(branch);
    }

    @PutMapping("/{gln}")
    public BranchDTO updateBranch(@PathVariable Long gln, @RequestBody BranchDTO branchDTO) {
        Branch branch = branchMapper.convertToEntity(branchDTO);
        Branch updated = branchService.updateByGln(gln, branch);
        return branchMapper.convertToDTO(updated);
    }

    @DeleteMapping("/{gln}")
    public boolean deleteBranch(@PathVariable Long gln) {
        return branchService.deleteByGln(gln);
    }
}