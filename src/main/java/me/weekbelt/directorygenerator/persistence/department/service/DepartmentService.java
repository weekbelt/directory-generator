package me.weekbelt.directorygenerator.persistence.department.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.weekbelt.directorygenerator.persistence.common.PhoneType;
import me.weekbelt.directorygenerator.persistence.common.fileUtil.JsonExporter;
import me.weekbelt.directorygenerator.persistence.department.Department;
import me.weekbelt.directorygenerator.persistence.department.DepartmentTree;
import me.weekbelt.directorygenerator.persistence.department.NewDepartmentJson;
import me.weekbelt.directorygenerator.persistence.department.NewDepartmentJson.Hierarchy;
import me.weekbelt.directorygenerator.persistence.department.repository.DepartmentRepository;
import me.weekbelt.directorygenerator.persistence.department.repository.DepartmentTreeRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class DepartmentService {

    private final JsonExporter jsonExporter;
    private final DepartmentRepository departmentRepository;
    private final DepartmentTreeRepository departmentTreeRepository;


    public void saveAllDepartmentsAndDepartmentTrees(String branchName) throws JsonProcessingException {
        List<NewDepartmentJson> newDepartmentJsons = jsonExporter.getNewDepartmentJsons(branchName);

        for (NewDepartmentJson newDepartmentJson : newDepartmentJsons) {
            saveDepartment(newDepartmentJson);
            saveDepartmentTrees(newDepartmentJson);
        }
    }

    private void saveDepartment(NewDepartmentJson newDepartmentJson) {
        NewDepartmentJson.Phone phone = newDepartmentJson.getPhone();
        Department department = Department.builder()
            .id(String.valueOf(newDepartmentJson.getId()))
            .name(newDepartmentJson.getName())
            .phoneNumber(phone.getNumber())
            .phoneType(PhoneType.valueOf(phone.getType()))
            .branchId(newDepartmentJson.getBranchID())
            .build();
        departmentRepository.save(department);
    }

    private void saveDepartmentTrees(NewDepartmentJson newDepartmentJson) {
        List<Hierarchy> hierarchies = newDepartmentJson.getHierarchies();

        int depth = newDepartmentJson.getDepth();
        String departmentId = newDepartmentJson.getId();
        String branchId = newDepartmentJson.getBranchID();

        for (Hierarchy hierarchy : hierarchies) {
            if (depth < 0) {
                throw new IllegalStateException("depth??? 0 ????????????????????????.");
            }
            String ancestorDepartmentId = hierarchy.getId();
            DepartmentTree departmentTree = DepartmentTree.builder()
                .id(UUID.randomUUID().toString())
                .ancestor(ancestorDepartmentId)
                .descendant(departmentId)
                .depth(depth--)
                .branchId(branchId)
                .build();

            departmentTreeRepository.save(departmentTree);
        }
        DepartmentTree departmentTree = DepartmentTree.builder()
            .id(UUID.randomUUID().toString())
            .ancestor(departmentId)
            .descendant(departmentId)
            .depth(0)
            .branchId(branchId)
            .build();
        departmentTreeRepository.save(departmentTree);
    }
}
