package com.vencill.pdm_lifecycle_manager.service;

import com.vencill.pdm_lifecycle_manager.domain.lifecyclestate;
import com.vencill.pdm_lifecycle_manager.domain.PartObject;
import com.vencill.pdm_lifecycle_manager.repository.PartObjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LifecycleService {

    private final PartObjectRepository partRepo;
    public LifecycleService(PartObjectRepository partRepo){
        this.partRepo = partRepo;
    }

    private static final Map<lifecyclestate, List<lifecyclestate>> TRANSITIONS = Map.of(
            lifecyclestate.DRAFT, List.of(lifecyclestate.IN_REVIEW),
            lifecyclestate.IN_REVIEW, List.of(lifecyclestate.RELEASED, lifecyclestate.DRAFT),
            lifecyclestate.RELEASED, List.of(lifecyclestate.OBSOLETE),
            lifecyclestate.OBSOLETE, List.of()
    );

    // make a new part
    public PartObject createPart(PartObject part) {
        return partRepo.save(part);
    }

    // get a part by ID
    public Optional<PartObject> getPartById(long Id){
        return partRepo.findById(Id);
    }

    // get all parts
    public List<PartObject> getAllParts(){
        return partRepo.findAll();
    }

    // get children of a part
    public List<PartObject> getChildren(Long parentId){
        return partRepo.findByParentId(parentId);
    }

    // transition part to new life cycle state
    public PartObject transition(Long partId, lifecyclestate targetState){
        PartObject part = partRepo.findById(partId)
                .orElseThrow(() -> new RuntimeException("Part not found: " + partId));

        List<lifecyclestate> allowed = TRANSITIONS.get(part.getState());
        if (!allowed.contains(targetState)) {
            throw new IllegalStateException(
                    "Cannot transition from " + part.getState() + " to " + targetState
            );
        }
        validateTransition(part, targetState);
        part.setState(targetState);
        return partRepo.save(part);
    }

    //Validation rules before a transition
    private void validateTransition(PartObject part, lifecyclestate target){
        if (target == lifecyclestate.RELEASED && part.getPartNumber() == null){
            throw new IllegalStateException("Part number is required before releasing a part.");
        }
    }

}
