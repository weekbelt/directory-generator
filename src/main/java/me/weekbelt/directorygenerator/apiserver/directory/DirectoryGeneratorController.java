package me.weekbelt.directorygenerator.apiserver.directory;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import me.weekbelt.directorygenerator.persistence.department.service.DepartmentJsonService;
import me.weekbelt.directorygenerator.persistence.department.service.DepartmentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DirectoryGeneratorController {

    private final DepartmentService departmentService;
    private final DepartmentJsonService departmentJsonService;

    @GetMapping("/api/v1/directory/new-generator/{branchName}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Resource> generateDepartment(@PathVariable String branchName) throws IOException {
        departmentService.saveAllDepartmentsAndDepartmentTrees(branchName);
        Resource resource = departmentJsonService.generateDirectoryDepartment(branchName);
        return getResourceResponseEntity(resource, branchName);
    }

    private ResponseEntity<Resource> getResourceResponseEntity(Resource departmentJsonResource, String fileName) throws IOException {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; fileName=" + fileName)
            .contentLength(departmentJsonResource.contentLength())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(departmentJsonResource);
    }
}
