package me.weekbelt.directorygenerator.persistence.staffer;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import me.weekbelt.directorygenerator.persistence.BaseTimeEntity;
import me.weekbelt.directorygenerator.persistence.position.Position;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class StafferPosition extends BaseTimeEntity {

    @Id
    private String id;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "staffer_id")
    private Staffer staffer;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @Column
    private String branchId;

}
