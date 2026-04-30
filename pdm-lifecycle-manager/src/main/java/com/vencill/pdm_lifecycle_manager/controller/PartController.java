package com.vencill.pdm_lifecycle_manager.controller;

import com.vencill.pdm_lifecycle_manager.domain.PartObject;
import com.vencill.pdm_lifecycle_manager.domain.lifecyclestate;
import com.vencill.pdm_lifecycle_manager.service.LifecycleService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/parts")

public class PartController {
    private final LifecycleService cycleService;
    public PartController(LifecycleService cycleService) {
        this.cycleService = cycleService;
    }

    // create new part
    @PostMapping
    public ResponseEntity<PartObject> createPart(@RequestBody PartObject part){
        return ResponseEntity.ok(cycleService.createPart(part));
    }

    // get a part by its ID
    @GetMapping("/{id}")
    public ResponseEntity<PartObject> getPartById(@PathVariable Long id){
        return cycleService.getPartById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // get all parts
    @GetMapping
    public ResponseEntity<List<PartObject>> getAllParts(){
        return ResponseEntity.ok(cycleService.getAllParts());
    }

    // get the children of a part
    @GetMapping("/{id}/children")
    public ResponseEntity<List<PartObject>> getChildren(@PathVariable Long id){
        return ResponseEntity.ok(cycleService.getChildren(id));
    }

    // transition a part to a new life cycle state
    @PutMapping("/{id}/transition")
    public ResponseEntity<PartObject> transition(
            @PathVariable Long id,
            @RequestParam lifecyclestate targetState) {
        return ResponseEntity.ok(cycleService.transition(id, targetState));
    }

    // handle otherwise uncaught errors
    @ExceptionHandler({IllegalStateException.class, RuntimeException.class})
    public ResponseEntity<String> handleErrors(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
