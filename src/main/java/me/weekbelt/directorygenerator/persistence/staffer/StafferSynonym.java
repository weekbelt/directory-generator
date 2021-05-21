package me.weekbelt.directorygenerator.persistence.staffer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
public class StafferSynonym {

    @Id
    private String id;

    private String synonym;

    @ManyToOne
    @JoinColumn(name = "staffer_id")
    private Staffer staffer;

    @Column(nullable = false)
    private String branchId;

}
