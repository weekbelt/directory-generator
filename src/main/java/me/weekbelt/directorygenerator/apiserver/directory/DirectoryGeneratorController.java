package me.weekbelt.directorygenerator.apiserver.directory;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import me.weekbelt.directorygenerator.persistence.Job.service.JobJsonService;
import me.weekbelt.directorygenerator.persistence.department.service.DepartmentJsonService;
import me.weekbelt.directorygenerator.persistence.department.service.DepartmentService;
import me.weekbelt.directorygenerator.persistence.staffer.service.StafferJsonService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class DirectoryGeneratorController {

    private final DepartmentService departmentService;
    private final DepartmentJsonService departmentJsonService;
    private final StafferJsonService stafferJsonService;
    private final JobJsonService jobJsonService;


    @GetMapping("/v1/generate/new-department")
    public ResponseEntity<Resource> generateNewDepartment(String branchName) throws IOException {
        Resource newDepartmentJsonResource = departmentJsonService.generateNewDepartment(branchName);
        return getResourceResponseEntity(newDepartmentJsonResource, "department.json");
    }

    @GetMapping("/v1/generate/new-staffer")
    public ResponseEntity<Resource> generateNewStaffer(String branchName) throws IOException {
        Resource newStafferJsonResource = stafferJsonService.generateNewStaffer(branchName);
        return getResourceResponseEntity(newStafferJsonResource, "staffer.json");
    }

    @GetMapping("/v1/generate/new-job")
    public ResponseEntity<Resource> generateNewJob(String branchName) throws IOException {
        Resource newJobJsonResource = jobJsonService.generateNewJob(branchName);
        return getResourceResponseEntity(newJobJsonResource, "job.json");
    }

//    @GetMapping("/api/v1/directories/new-generator/{branchName}")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<Resource> generateDepartment(@PathVariable String branchName) throws IOException {
//        departmentService.saveAllDepartmentsAndDepartmentTrees(branchName);
////        departmentService.changeDepartmentIdToUUID();
//        Resource resource = departmentJsonService.generateDirectoryDepartment(branchName);
//        return getResourceResponseEntity(resource, branchName);
//    }

    private ResponseEntity<Resource> getResourceResponseEntity(Resource departmentJsonResource, String fileName) throws IOException {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; fileName=" + fileName)
            .contentLength(departmentJsonResource.contentLength())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(departmentJsonResource);
    }
}
