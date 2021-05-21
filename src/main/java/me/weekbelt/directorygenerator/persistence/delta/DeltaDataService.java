package me.weekbelt.directorygenerator.persistence.delta;

import com.posicube.robi.reception.apiserver.delta.dto.DeltaSearchCondition;
import com.posicube.robi.reception.persistence.exception.EntityNotExistException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class DeltaDataService {

    private final DeltaRepository deltaRepository;

    public Delta getDeltaById(String deltaId) {
        return this.deltaRepository.findById(deltaId).orElseThrow(() -> new EntityNotExistException(Delta.class.getName(), "id", deltaId));
    }

    public List<Delta> getNotAppliedDeltas(String branchId) {
        return this.deltaRepository.findByApplyAndBranchId(false, branchId);
    }

    public List<Delta> getDeltasByApplyAndReportedDate(boolean isApply, LocalDate reportedDate) {
        return this.deltaRepository.findByApplyAndReportedDate(isApply, reportedDate);
    }

    public List<Delta> getNotReportedDeltas() {
        return this.deltaRepository.findByIsReport(false);
    }

    public void addDelta(Delta delta) {
        this.deltaRepository.save(delta);
    }

    public Page<Delta> getDeltasBySearchCondition(DeltaSearchCondition deltaSearchCondition, Pageable pageable) {
        return this.deltaRepository.findBySearchCondition(deltaSearchCondition, pageable);
    }

    public void deleteDeltaById(String deltaId) {
        this.deltaRepository.deleteById(deltaId);
    }
}
