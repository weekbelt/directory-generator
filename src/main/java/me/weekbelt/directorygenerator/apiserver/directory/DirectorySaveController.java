package me.weekbelt.directorygenerator.apiserver.directory;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import me.weekbelt.directorygenerator.persistence.department.service.DepartmentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class DirectorySaveController {

    private final DepartmentService departmentService;

    @PostMapping("/v1/departments/list")
    public void saveAllDepartments(String branchName) throws JsonProcessingException {
        departmentService.saveAllDepartmentsAndDepartmentTrees(branchName);
    }
}
