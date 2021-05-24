package me.weekbelt.directorygenerator.persistence.department.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.weekbelt.directorygenerator.persistence.common.PhoneType;
import me.weekbelt.directorygenerator.persistence.common.fileUtil.JsonExporter;
import me.weekbelt.directorygenerator.persistence.department.Department;
import me.weekbelt.directorygenerator.persistence.department.DepartmentTree;
import me.weekbelt.directorygenerator.persistence.department.NewDepartmentJson;
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
        String departmentId = newDepartmentJson.getId();
        String parentDepartmentId = newDepartmentJson.getParentDept() == null ? null : newDepartmentJson.getParentDept().getId();
        String branchId = newDepartmentJson.getBranchID();

        List<DepartmentTree> departmentTrees = getDepartmentTrees(parentDepartmentId, departmentId, branchId);
        departmentTreeRepository.saveAll(departmentTrees);
    }

    public List<DepartmentTree> getDepartmentTrees(String ancestorId, String departmentId, String branchId) {
        List<DepartmentTree> parentDepartmentTrees = departmentTreeRepository.findByDescendantAndBranchId(ancestorId, branchId);
        List<DepartmentTree> departmentTrees = new ArrayList<>();
        parentDepartmentTrees.forEach(parentDepartmentTree -> {
            DepartmentTree departmentTree = DepartmentTree.builder()
                .id(UUID.randomUUID().toString())
                .ancestor(parentDepartmentTree.getAncestor())
                .descendant(departmentId)
                .depth(parentDepartmentTree.getDepth() + 1)
                .branchId(parentDepartmentTree.getBranchId())
                .build();
            departmentTrees.add(departmentTree);
        });

        departmentTrees.add(DepartmentTree.builder()
            .id(UUID.randomUUID().toString())
            .ancestor(departmentId)
            .descendant(departmentId)
            .depth(0)
            .branchId(branchId)
            .build());
        return departmentTrees;
    }
}
