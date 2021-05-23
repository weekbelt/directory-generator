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
import me.weekbelt.directorygenerator.persistence.department.OldDepartmentJson;
import me.weekbelt.directorygenerator.persistence.department.OldDepartmentJson.Hierarchy;
import me.weekbelt.directorygenerator.persistence.department.OldDepartmentJson.Phone;
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
        List<OldDepartmentJson> oldDepartmentJsons = jsonExporter.getOldDepartmentJsons(branchName);
        saveAllDepartments(oldDepartmentJsons);
        saveAllDepartmentTrees(oldDepartmentJsons);
    }

    private void saveAllDepartments(List<OldDepartmentJson> oldDepartmentJsons) throws JsonProcessingException {
        for (OldDepartmentJson oldDepartmentJson : oldDepartmentJsons) {
            Phone phone = oldDepartmentJson.getPhone();

            Department department = Department.builder()
                .id(UUID.randomUUID().toString())
                .name(oldDepartmentJson.getDeptName())
                .phoneNumber(phone.getNumber())
                .phoneType(phone.getType().equals("내선") ? PhoneType.INWARD_DIALING : PhoneType.OUTWARD_DIALING)
                .branchId(oldDepartmentJson.getBchID())
                .build();
            departmentRepository.save(department);
        }
    }

    private void saveAllDepartmentTrees(List<OldDepartmentJson> oldDepartmentJsons) {
        oldDepartmentJsons.forEach(oldDepartmentJson -> {
            List<DepartmentTree> departmentTrees = getDepartmentTrees(oldDepartmentJson);
            departmentTreeRepository.saveAll(departmentTrees);
        });
    }

    private List<DepartmentTree> getDepartmentTrees(OldDepartmentJson oldDepartmentJson) {
        List<DepartmentTree> departmentTrees = new ArrayList<>();
        addAncestorDepartmentTrees(oldDepartmentJson, departmentTrees);
        addCurrentDepartmentTree(oldDepartmentJson, departmentTrees);
        return departmentTrees;
    }

    private void addCurrentDepartmentTree(OldDepartmentJson oldDepartmentJson, List<DepartmentTree> departmentTrees) {
        DepartmentTree departmentTree = DepartmentTree.builder()
            .id(UUID.randomUUID().toString())
            .ancestor(String.valueOf(oldDepartmentJson.getDepartmentID()))
            .descendant(String.valueOf(oldDepartmentJson.getDepartmentID()))
            .depth(0)
            .branchId(oldDepartmentJson.getBchID())
            .build();
        departmentTrees.add(departmentTree);
    }

    private void addAncestorDepartmentTrees(OldDepartmentJson oldDepartmentJson, List<DepartmentTree> departmentTrees) {
        List<Hierarchy> hierarchy = oldDepartmentJson.getHierarchy();
        if (!hierarchy.isEmpty()) {
            hierarchy
        }
    }
}
