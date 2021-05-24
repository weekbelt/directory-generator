package me.weekbelt.directorygenerator.persistence.department;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewDepartmentJson {

    private String id;

    private String name;

    private Phone phone;

    private ParentDept parentDept;

    private String branchID;

    private List<String> synonyms;

    private List<String> areas;

    public void setNewDepartmentId(String newDepartmentId) {
        this.id = newDepartmentId;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ParentDept {

        private String id;
        private String name;
        private Phone phone;

        public void setNewParentDepartmentId(String newDepartmentId) {
            this.id = newDepartmentId;
        }
    }


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Phone {

        private String type;
        private String number;
    }

}
