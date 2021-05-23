package me.weekbelt.directorygenerator.persistence.department.repository;

import me.weekbelt.directorygenerator.persistence.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, String> {

}
