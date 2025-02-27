package system.integration.branchInventory.Controller;

import org.springframework.web.bind.annotation.*;
import system.integration.branchInventory.domain.model.Batch;
import system.integration.branchInventory.service.BatchService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/batch")
public class BatchController {
    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping
    public List<Batch> getAllBatches() {
        return batchService.getAllBatches();
    }

    @GetMapping("/{id}")
    public Batch getBatchById(@PathVariable UUID id) {
        return batchService.getBatchById(id);
    }

    @PostMapping
    public Batch createBatch(@RequestBody Batch batch) {
        return batchService.saveBatch(batch);
    }

    @DeleteMapping("/{id}")
    public void deleteBatch(@PathVariable UUID id) {
        batchService.deleteBatch(id);
    }
}