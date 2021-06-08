package me.weekbelt.directorygenerator.persistence.department.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import me.weekbelt.directorygenerator.persistence.common.PhoneType;
import me.weekbelt.directorygenerator.persistence.common.fileUtil.JsonExporter;
import me.weekbelt.directorygenerator.persistence.department.Department;
import me.weekbelt.directorygenerator.persistence.department.DepartmentTree;
import me.weekbelt.directorygenerator.persistence.department.NewDepartmentJson;
import me.weekbelt.directorygenerator.persistence.department.NewDepartmentJson.Hierarchy;
import me.weekbelt.directorygenerator.persistence.department.repository.DepartmentRepository;
import me.weekbelt.directorygenerator.persistence.department.repository.DepartmentTreeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DepartmentServiceTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JsonExporter jsonExporter;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentTreeRepository departmentTreeRepository;

    @Test
    @DisplayName("DepartmentTree 제대로 저장 되는지 테스트")
    public void saveDepartmentTree_success() throws Exception {
        // given
        List<NewDepartmentJson> departmentJsons = jsonExporter.getNewDepartmentJsons("dongnae");

        // when
        for (NewDepartmentJson newDepartmentJson : departmentJsons) {
            saveDepartment(newDepartmentJson);
            saveDepartmentTrees(newDepartmentJson);
        }

        // then
        List<DepartmentTree> departmentTrees = departmentTreeRepository.findByDescendantAndBranchId("213a706c-949a-40d8-9468-1eb5419bdeab", "510aa611-5e3a-4e37-90a4-d5f98a8d2d48");

        for (DepartmentTree departmentTree : departmentTrees) {
            System.out.println("ancestor: " + departmentTree.getAncestor() + " descendant: " + departmentTree.getDescendant() + " depth: " + departmentTree.getDepth());
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
                throw new IllegalStateException("depth는 0 이상이어야합니다.");
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