package me.weekbelt.directorygenerator.persistence.Position;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class PositionSynonym {

    @Id
    private String id;

    private String synonym;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @Column(nullable = false)
    private String branchId;

}
