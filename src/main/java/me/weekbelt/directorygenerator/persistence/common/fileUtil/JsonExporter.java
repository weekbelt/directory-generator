package me.weekbelt.directorygenerator.persistence.common.fileUtil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.weekbelt.directorygenerator.persistence.common.exception.JsonFileHandlingException;
import me.weekbelt.directorygenerator.persistence.department.OldDepartmentJson;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonExporter {

    private final ObjectMapper objectMapper;

    public List<OldDepartmentJson> getOldDepartmentJsons(String branchName) {
        ClassPathResource departmentJsonResource = new ClassPathResource("json/" + branchName + "/old/department.json");
        try {
            File file = departmentJsonResource.getFile();
            return objectMapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new FileSystemNotFoundException(e.getMessage());
        }
    }

    public ByteArrayResource getByteArrayResource(List<?> jsonList) {
        try {
            String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonList);
            byte[] arrayBytes = result.getBytes(StandardCharsets.UTF_8);
            return new ByteArrayResource(arrayBytes);
        } catch (IOException e) {
            throw new JsonFileHandlingException("Json File Create Error", e);
        }
    }
}
