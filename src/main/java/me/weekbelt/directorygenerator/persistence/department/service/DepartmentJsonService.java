package me.weekbelt.directorygenerator.persistence.department.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import me.weekbelt.directorygenerator.persistence.common.fileUtil.JsonExporter;
import me.weekbelt.directorygenerator.persistence.department.NewDepartmentJson;
import me.weekbelt.directorygenerator.persistence.department.NewDepartmentJson.ParentDept;
import me.weekbelt.directorygenerator.persistence.department.OldDepartmentJson;
import me.weekbelt.directorygenerator.persistence.department.OldDepartmentJson.Phone;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DepartmentJsonService {

    private final ObjectMapper objectMapper;
    private final JsonExporter jsonExporter;

    public Resource generateDirectoryDepartment(String branchName) throws JsonProcessingException {
        List<OldDepartmentJson> oldDepartmentJsons = jsonExporter.getOldDepartmentJsons(branchName);
        for (OldDepartmentJson oldDepartmentJson : oldDepartmentJsons) {
            Phone phone = oldDepartmentJson.getPhone();

            NewDepartmentJson.builder()
                .id(UUID.randomUUID().toString())
                .name(oldDepartmentJson.getDeptName())
                .phone(phone)
                .build();
        }
    }
//
//    private Phone getPhone(OldDepartmentJson oldDepartmentJson) throws JsonProcessingException {
//        JsonNode phoneJsonNode = oldDepartmentJson.getPhone();
//        return objectMapper.treeToValue(phoneJsonNode, Phone.class);
//    }

    private ParentDept getParentDept(OldDepartmentJson oldDepartmentJson) {
        return Optional.ofNullable(oldDepartmentJson.getParentDept())
            .map(parentDept -> {

            })
            .orElse(null);
    }
}
