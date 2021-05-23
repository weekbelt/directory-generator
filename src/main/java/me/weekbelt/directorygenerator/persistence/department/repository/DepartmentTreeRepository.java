package me.weekbelt.directorygenerator.persistence.department.repository;

import me.weekbelt.directorygenerator.persistence.department.DepartmentTree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentTreeRepository extends JpaRepository<DepartmentTree, String> {

}
