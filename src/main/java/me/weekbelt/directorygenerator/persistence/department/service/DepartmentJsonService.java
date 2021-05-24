package me.weekbelt.directorygenerator.persistence.department.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.weekbelt.directorygenerator.persistence.common.fileUtil.JsonExporter;
import me.weekbelt.directorygenerator.persistence.department.Department;
import me.weekbelt.directorygenerator.persistence.department.DepartmentTree;
import me.weekbelt.directorygenerator.persistence.department.NewDepartmentJson;
import me.weekbelt.directorygenerator.persistence.department.NewDepartmentJson.ParentDept;
import me.weekbelt.directorygenerator.persistence.department.NewDepartmentJson.Phone;
import me.weekbelt.directorygenerator.persistence.department.OldDepartmentJson;
import me.weekbelt.directorygenerator.persistence.department.repository.DepartmentRepository;
import me.weekbelt.directorygenerator.persistence.department.repository.DepartmentTreeRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DepartmentJsonService {

    private final JsonExporter jsonExporter;
    private final DepartmentRepository departmentRepository;
    private final DepartmentTreeRepository departmentTreeRepository;

    public Resource generateDirectoryDepartment(String branchName) {
        String branchId = getBranchId(branchName);

        List<NewDepartmentJson> newDepartmentJsons = new ArrayList<>();
        List<Department> departments = departmentRepository.findAllByBranchId(branchId);
        for (Department department : departments) {
            NewDepartmentJson newDepartmentJson = getNewDepartmentJson(department);
            newDepartmentJsons.add(newDepartmentJson);
        }
        return jsonExporter.getByteArrayResource(newDepartmentJsons);
    }

    private String getBranchId(String branchName) {
        List<OldDepartmentJson> oldDepartmentJsons = jsonExporter.getOldDepartmentJsons(branchName);
        return oldDepartmentJsons.get(0).getBchID();
    }

    private NewDepartmentJson getNewDepartmentJson(Department department) {
        Phone phone = getPhone(department);
        ParentDept parentDept = getParentDept(department);

        return NewDepartmentJson.builder()
            .id(UUID.randomUUID().toString())
            .name(department.getName())
            .phone(phone)
            .branchID(department.getBranchId())
            .parentDept(parentDept)
            .synonyms(new ArrayList<>())
            .build();
    }

    private NewDepartmentJson.Phone getPhone(Department department) {
        return NewDepartmentJson.Phone.builder()
            .number(department.getPhoneNumber())
            .type(department.getPhoneType().name())
            .build();
    }

    private ParentDept getParentDept(Department department) {
        Department parentDepartment = getParentDepartment(department);
        return ParentDept.builder()
            .id(parentDepartment.getId())
            .name(parentDepartment.getName())
            .phone(Phone.builder().type(parentDepartment.getPhoneType().name()).number(parentDepartment.getPhoneNumber()).build())
            .build();
    }

    private Department getParentDepartment(Department department) {
        DepartmentTree parentDepartmentTree = departmentTreeRepository.findByDescendantAndDepth(department.getId(), 1)
            .orElseThrow(() -> new EntityNotFoundException("해당 DepartmentTree를 찾을 수 없습니다. descendant=" + department.getId() + " depth=1"));
        String parentDepartmentId = parentDepartmentTree.getAncestor();
        return departmentRepository.findById(parentDepartmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 Department를 찾을 수 없습니다. departmentId=" + parentDepartmentId));
    }
}
