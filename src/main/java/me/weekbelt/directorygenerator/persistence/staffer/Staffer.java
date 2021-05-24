package me.weekbelt.directorygenerator.persistence.staffer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.weekbelt.directorygenerator.persistence.BaseTimeEntity;
import me.weekbelt.directorygenerator.persistence.common.PhoneType;
import me.weekbelt.directorygenerator.persistence.department.Department;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Staffer extends BaseTimeEntity {

    @Id
    private String id; // manual generate uuid

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PhoneType phoneType;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column
    private String areas;

    @JsonBackReference
    @Builder.Default
    @OneToMany(mappedBy = "staffer", fetch = FetchType.LAZY)
    private List<StafferJob> jobs = new ArrayList<>();

    @JsonBackReference
    @Builder.Default
    @OneToMany(mappedBy = "staffer", fetch = FetchType.LAZY)
    private List<StafferPosition> positions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "staffer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StafferSynonym> synonyms = new ArrayList<>();

    @Column(nullable = false)
    private String branchId;

    public void addDepartment(Department department) {
        this.department = department;
    }
}
