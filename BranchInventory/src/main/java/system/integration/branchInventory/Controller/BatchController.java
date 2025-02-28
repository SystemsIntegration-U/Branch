package system.integration.branchInventory.Controller;

import org.springframework.web.bind.annotation.*;
import system.integration.branchInventory.dto.BatchDTO;
import system.integration.branchInventory.mappers.BatchMapper;
import system.integration.branchInventory.repository.IMedicineRepository;
import system.integration.branchInventory.service.BatchService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/batch")
public class BatchController {

    private final BatchService batchService;
    private final BatchMapper batchMapper;

    public BatchController(BatchService batchService, BatchMapper batchMapper, IMedicineRepository medicineRepository) {
        this.batchService = batchService;
        this.batchMapper = batchMapper;
    }

    @GetMapping
    public List<BatchDTO> getAllBatches() {
        return batchService.getAll().stream()
                .map(batchMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BatchDTO getBatchById(@PathVariable UUID id) {
        return batchMapper.toDTO(batchService.getById(id).orElse(null));
    }

    @PostMapping
    public BatchDTO createBatch(@RequestBody BatchDTO batchDTO) {
        return batchService.saveBatch(batchDTO);
    }
    
    @DeleteMapping("/{id}")
    public void deleteBatch(@PathVariable UUID id) {
        batchService.delete(id);
    }
}