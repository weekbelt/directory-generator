package me.weekbelt.directorygenerator.persistence.staffer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StafferConverter {

    public static NewStafferJson convertToNewStaffer(OldStafferJson oldStafferJson) {
        ObjectMapper objectMapper = new ObjectMapper();

        NewStafferJson.Phone phone = createPhone(oldStafferJson);
        NewStafferJson.Phone newPhone2 = createPhone2(oldStafferJson, objectMapper);
        List<String> jobs = createJobs(oldStafferJson);
        List<String> positions = createPositions(oldStafferJson);

        return NewStafferJson.builder()
            .id(UUID.randomUUID().toString())
            .name(oldStafferJson.getFullName())
            .stafferPhone(phone)
            .departmentPhone(newPhone2)
            .departmentId(String.valueOf(oldStafferJson.getDepartmentID()))
            .departmentName(oldStafferJson.getDepartment())
            .jobs(jobs)
            .positions(positions)
            .branchId(oldStafferJson.getBchID())
            .areas(new ArrayList<>())
            .build();
    }

    private static NewStafferJson.Phone createPhone(OldStafferJson stafferJson) {
        return Optional.ofNullable(stafferJson.getPhone())
            .map(phone -> NewStafferJson.Phone.builder()
                .number(phone.getNumber())
                .type(phone.getType().equals("내선") ? "INWARD_DIALING" : "OUTWARD_DIALING")
                .build())
            .orElse(null);
    }

    private static NewStafferJson.Phone createPhone2(OldStafferJson stafferJson, ObjectMapper objectMapper) {
        Optional<JsonNode> jsonPhone2 = Optional.ofNullable(stafferJson.getPhone2());
        return jsonPhone2.map(phone2JsonNode -> {
            OldStafferJson.Phone phone2 = getOldStafferPhone2(stafferJson, objectMapper);
            return NewStafferJson.Phone.builder()
                .type(phone2.getType().equals("내선") ? "INWARD_DIALING" : "OUTWARD_DIALING")
//                .type("INWARD_DIALING")
                .number(phone2.getNumber() == null ? "0000" : phone2.getNumber())
                .build();
        }).orElse(null);
    }

    private static OldStafferJson.Phone getOldStafferPhone2(OldStafferJson stafferJson, ObjectMapper objectMapper) {
        OldStafferJson.Phone phone2 = null;
        try {
            phone2 = objectMapper.treeToValue(stafferJson.getPhone2(), OldStafferJson.Phone.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return phone2;
    }

    private static List<String> createJobs(OldStafferJson stafferJson) {
        List<String> jobs = new ArrayList<>();
        stafferJson.getJobs().forEach(job -> jobs.add(job.trim()));
        return jobs;
    }

    private static List<String> createPositions(OldStafferJson stafferJson) {
        List<String> positions = new ArrayList<>();
        stafferJson.getPositions().forEach(position -> positions.add(position.trim()));
        return positions;
    }
}
