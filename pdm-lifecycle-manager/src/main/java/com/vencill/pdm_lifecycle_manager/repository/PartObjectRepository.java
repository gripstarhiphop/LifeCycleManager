package com.vencill.pdm_lifecycle_manager.repository;

import com.vencill.pdm_lifecycle_manager.domain.PartObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PartObjectRepository extends JpaRepository<PartObject, Long>{
    List<PartObject> findByState(com.vencill.pdm_lifecycle_manager.domain.lifecyclestate state);

    List<PartObject> findByParentId(Long parentId);

    java.util.Optional<PartObject> findByPartNumber(String partNumber);
}
