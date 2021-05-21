package me.weekbelt.directorygenerator.persistence.delta;

import static com.posicube.robi.reception.persistence.delta.QDelta.delta;

import com.posicube.robi.reception.apiserver.delta.dto.DeltaSearchCondition;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class DeltaRepositoryImpl implements DeltaRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Delta> findBySearchCondition(DeltaSearchCondition deltaSearchCondition, Pageable pageable) {

        List<Delta> results = this.jpaQueryFactory.selectFrom(delta)
            .where(this.eqEntityType(deltaSearchCondition.getEntityType()),
                this.eqActionType(deltaSearchCondition.getActionType()),
                this.eqChangeRequestDate(deltaSearchCondition.getChangeRequestDate()),
                this.eqAppliedFlag(deltaSearchCondition.getAppliedFlag())
            )
            .orderBy(delta.createdDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Delta> countQuery = this.jpaQueryFactory.selectFrom(delta)
            .where(this.eqEntityType(deltaSearchCondition.getEntityType()),
                this.eqActionType(deltaSearchCondition.getActionType()),
                this.eqChangeRequestDate(deltaSearchCondition.getChangeRequestDate()),
                this.eqAppliedFlag(deltaSearchCondition.getAppliedFlag()),
                this.eqBranchId(deltaSearchCondition.getBranchId())
            );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchCount);
    }

    @Override
    public List<Delta> findByApplyAndBranchId(boolean isApply, String branchId) {
        return this.jpaQueryFactory.selectFrom(delta)
            .where(this.eqAppliedFlag(isApply),
                this.eqBranchId(branchId))
            .orderBy(delta.createdDate.desc())
            .fetch();
    }

    @Override
    public List<Delta> findByApplyAndReportedDate(boolean isApply, LocalDate reportedDate) {
        return this.jpaQueryFactory.selectFrom(delta)
            .where(this.eqAppliedFlag(isApply),
                this.loeReportedDate(reportedDate))
            .orderBy(delta.createdDate.desc())
            .fetch();
    }

    private BooleanExpression loeReportedDate(LocalDate reportedDate) {
        return reportedDate != null ? delta.reportedDate.loe(reportedDate) : null;
    }

    private BooleanExpression eqEntityType(EntityType entityType) {
        return entityType != null ? delta.entityType.eq(entityType) : null;
    }

    private Predicate eqActionType(ActionType actionType) {
        return actionType != null ? delta.actionType.eq(actionType) : null;
    }

    private BooleanExpression eqChangeRequestDate(LocalDate changeRequestDate) {
        return changeRequestDate != null ? delta.reportedDate.eq(changeRequestDate) : null;
    }

    private BooleanExpression eqAppliedFlag(Boolean appliedFlag) {
        return appliedFlag != null ? delta.isApply.eq(appliedFlag) : null;
    }

    private BooleanExpression eqBranchId(String branchId) {
        return delta.branchId.eq(branchId);
    }

}
