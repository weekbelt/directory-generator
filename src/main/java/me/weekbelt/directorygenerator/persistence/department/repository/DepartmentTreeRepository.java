package me.weekbelt.directorygenerator.persistence.department.repository;

import java.util.List;
import java.util.Optional;
import me.weekbelt.directorygenerator.persistence.department.DepartmentTree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentTreeRepository extends JpaRepository<DepartmentTree, String> {

    List<DepartmentTree> findByDescendantAndBranchId(String descendant, String branchId);

    List<DepartmentTree> findByAncestorAndBranchId(String ancestor, String branchId);

    Optional<DepartmentTree> findByDescendantAndDepth(String descendant, int depth);
}
