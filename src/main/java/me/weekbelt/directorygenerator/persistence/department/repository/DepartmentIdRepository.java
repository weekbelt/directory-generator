package me.weekbelt.directorygenerator.persistence.department.repository;

import java.util.Optional;
import me.weekbelt.directorygenerator.persistence.department.DepartmentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentIdRepository extends JpaRepository<DepartmentId, Long> {

    Optional<DepartmentId> findByOldDepartmentId(Long oldDepartmentId);
}
