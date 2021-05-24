package me.weekbelt.directorygenerator.persistence.staffer.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.weekbelt.directorygenerator.persistence.common.fileUtil.JsonExporter;
import me.weekbelt.directorygenerator.persistence.department.DepartmentId;
import me.weekbelt.directorygenerator.persistence.department.repository.DepartmentIdRepository;
import me.weekbelt.directorygenerator.persistence.staffer.NewStafferJson;
import me.weekbelt.directorygenerator.persistence.staffer.OldStafferJson;
import me.weekbelt.directorygenerator.persistence.staffer.StafferConverter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StafferJsonService {

    private final JsonExporter jsonExporter;
    private final DepartmentIdRepository departmentIdRepository;

    public Resource generateNewStaffer(String branchName) {
        List<OldStafferJson> oldStafferJsonList = jsonExporter.getOldStafferJsons(branchName);

        List<NewStafferJson> newStafferJsonList = oldStafferJsonList.stream().map(StafferConverter::convertToNewStaffer).collect(Collectors.toList());

        setNewDepartmentId(newStafferJsonList);

        return jsonExporter.getByteArrayResource(newStafferJsonList);
    }

    private void setNewDepartmentId(List<NewStafferJson> newStafferJsonList) {
        newStafferJsonList.forEach(newStafferJson -> {
            DepartmentId departmentId = departmentIdRepository.findByOldDepartmentId(Long.valueOf(newStafferJson.getDepartmentId()))
                .orElseThrow(() -> new EntityNotFoundException("해당하는 departmentId를 찾지 못했습니다. oldDepartmentId=" + newStafferJson.getDepartmentId()));
            newStafferJson.setNewDepartmentId(departmentId.getNewDepartmentId());
        });
    }

}
