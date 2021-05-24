package me.weekbelt.directorygenerator.persistence.position;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.weekbelt.directorygenerator.persistence.staffer.StafferPosition;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Position {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String branchId;

    @JsonManagedReference
    @Builder.Default
    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PositionSynonym> synonyms = new ArrayList<>();

    @JsonBackReference
    @Builder.Default
    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY)
    private List<StafferPosition> staffers = new ArrayList<>();

}
