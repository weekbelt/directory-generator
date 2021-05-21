package me.weekbelt.directorygenerator.persistence.delta;

import com.posicube.robi.reception.apiserver.delta.dto.DeltaSearchCondition;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeltaRepositoryCustom {

    Page<Delta> findBySearchCondition(DeltaSearchCondition deltaSearchCondition, Pageable pageable);

    List<Delta> findByApplyAndBranchId(boolean isApply, String branchId);

    List<Delta> findByApplyAndReportedDate(boolean isApply, LocalDate reportedDate);
}
