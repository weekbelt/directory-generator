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
            OldDepartmentJson.Phone phone = oldDepartmentJson.getOldDepartmentPhone();

            Department department = Department.builder()
                .id(String.valueOf(oldDepartmentJson.getDepartmentID()))
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
        List<Hierarchy> departmentHierarchy = oldDepartmentJson.getHierarchy();
        int depth = oldDepartmentJson.getDepth();

        for (Hierarchy hierarchy : departmentHierarchy) {
            DepartmentTree departmentTree = DepartmentTree.builder()
                .id(UUID.randomUUID().toString())
                .ancestor(hierarchy.getId())
                .descendant(String.valueOf(oldDepartmentJson.getDepartmentID()))
                .depth(depth)
                .branchId(oldDepartmentJson.getBchID())
                .build();
            departmentTrees.add(departmentTree);
            depth--;
        }
    }

    private void changeDepartmentIdToUUID() {
        List<Department> departments = departmentRepository.findAll();
        for (Department department : departments) {
            String oldDepartmentId = department.getId();
            String newDepartmentId = UUID.randomUUID().toString();

            department.changeId(newDepartmentId);
            departmentRepository.save(department);

            List<DepartmentTree> departmentTreesForAncestor = departmentTreeRepository.findByAncestorAndBranchId(oldDepartmentId, department.getBranchId());
            for (DepartmentTree departmentTree : departmentTreesForAncestor) {
                departmentTree.changeAncestorId(newDepartmentId);
            }
            departmentTreeRepository.saveAll(departmentTreesForAncestor);

            List<DepartmentTree> departmentTrees = departmentTreeRepository.findByDescendantAndBranchId(oldDepartmentId, department.getBranchId());
            for (DepartmentTree departmentTree : departmentTrees) {
                departmentTree.changeDescendantId(newDepartmentId);
            }
            departmentTreeRepository.saveAll(departmentTrees);
        }
    }

}
