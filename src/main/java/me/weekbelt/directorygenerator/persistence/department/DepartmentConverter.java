package me.weekbelt.directorygenerator.persistence.department;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import me.weekbelt.directorygenerator.persistence.department.NewDepartmentJson.Hierarchy;
import me.weekbelt.directorygenerator.persistence.department.NewDepartmentJson.ParentDept;
import me.weekbelt.directorygenerator.persistence.department.NewDepartmentJson.Phone;

public class DepartmentConverter {

    public static NewDepartmentJson convertToNewDepartmentJson(OldDepartmentJson oldDepartmentJson, ObjectMapper objectMapper) {
        Phone phone = createPhone(oldDepartmentJson, objectMapper);
        ParentDept parentDept = createParentDept(oldDepartmentJson);
        List<Hierarchy> hierarchies = oldDepartmentJson.getHierarchy().stream().map(hierarchy ->
            Hierarchy.builder()
                .id(hierarchy.getId())
                .name(hierarchy.getName())
                .build()
        ).collect(Collectors.toList());

        return NewDepartmentJson.builder()
            .id(String.valueOf(oldDepartmentJson.getDepartmentID()))
            .name(oldDepartmentJson.getDeptName())
            .phone(phone)
            .parentDept(parentDept)
            .branchID(oldDepartmentJson.getBchID())
            .synonyms(new ArrayList<>())
            .depth(hierarchies.size())
            .hierarchies(hierarchies)
            .build();
    }

    private static Phone createPhone(OldDepartmentJson departmentJson, ObjectMapper objectMapper) {
        Optional<JsonNode> phone = Optional.ofNullable(departmentJson.getPhone());
        return phone.map(phoneJsonNode -> {
            Phone oldDepartmentPhone = getOldDepartmentPhone(objectMapper, phoneJsonNode);
            return NewDepartmentJson.Phone.builder()
                .type(Objects.requireNonNull(oldDepartmentPhone).getType().equals("내선") ? "INWARD_DIALING" : "OUTWARD_DIALING")
                .number(oldDepartmentPhone.getNumber())
                .build();
        }).orElse(null);
    }

    private static Phone getOldDepartmentPhone(ObjectMapper objectMapper, JsonNode phoneJsonNode) {
        Phone oldDepartmentPhone = null;
        try {
            oldDepartmentPhone = objectMapper.treeToValue(phoneJsonNode, Phone.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return oldDepartmentPhone;
    }

    private static ParentDept createParentDept(OldDepartmentJson departmentJson) {
        Optional<OldDepartmentJson.ParentDept> optionalParentDept = Optional.ofNullable(departmentJson.getParentDept());
        return optionalParentDept.map(parentDept -> {
            Phone phone = Phone.builder()
                .number(parentDept.getPhone().getNumber())
//                .type("INWARD_DIALING")
                .type(Objects.requireNonNull(parentDept.getPhone()).getType().equals("내선") ? "INWARD_DIALING" : "OUTWARD_DIALING")
                .build();
            return NewDepartmentJson.ParentDept.builder()
                .id(String.valueOf(parentDept.getId()))
                .name(parentDept.getName())
                .phone(phone)
                .build();
        }).orElse(null);
    }

}
