package me.weekbelt.directorygenerator.persistence.department;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Entity
@Getter
public class DepartmentSynonym {

    @Id
    private String id;

    @Column
    private String synonym;

    @Column(nullable = false)
    private String branchId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
