package me.weekbelt.directorygenerator.persistence.department;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.weekbelt.directorygenerator.persistence.common.PhoneType;
import me.weekbelt.directorygenerator.persistence.staffer.Staffer;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Getter
@Entity
public class Department {

    @Id
    private String id;

    @Column(nullable = false)
    public String name;

    @Builder.Default
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<DepartmentSynonym> synonyms = new ArrayList<>();

    @Column
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PhoneType phoneType;

    @Column
    private String areas;

    @Builder.Default
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Staffer> staffers = new ArrayList<>();

    @Column(nullable = false)
    private String branchId;

}
