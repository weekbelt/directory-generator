package me.weekbelt.directorygenerator.persistence.delta;

import com.fasterxml.jackson.databind.JsonNode;
import com.posicube.robi.reception.apiserver.common.converter.JsonNodeConverter;
import com.posicube.robi.reception.apiserver.delta.dto.DeltaCreationRequestDto;
import com.posicube.robi.reception.apiserver.delta.dto.DeltaDto;
import com.posicube.robi.reception.persistence.BaseTimeEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.weekbelt.directorygenerator.persistence.BaseTimeEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Delta extends BaseTimeEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String originalId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @Column(columnDefinition = "json", nullable = false)
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode changedData;

    @Column(nullable = false)
    private boolean isReport;

    @Column
    private LocalDate reportedDate;

    @Column(nullable = false)
    private boolean isApply;

    @Column
    private LocalDateTime appliedDate;

    @Column(nullable = false)
    private String branchId;

    public DeltaDto convertToDto() {
        return DeltaDto.builder()
            .id(this.id)
            .entityType(this.entityType)
            .actionType(this.actionType)
            .changedData(this.changedData)
            .reportedDate(this.reportedDate)
            .isApply(this.isApply)
            .build();
    }

    public DeltaCreationRequestDto convertToCreationDto() {
        return DeltaCreationRequestDto.builder()
            .entityType(this.entityType)
            .actionType(this.actionType)
            .changedData(this.changedData)
            .branchId(this.branchId)
            .originalId(this.originalId)
            .build();
    }

    public void reportComplete() {
        this.isReport = true;
    }

    public void applyComplete() {
        this.isApply = true;
        this.appliedDate = LocalDateTime.now();
    }

}
