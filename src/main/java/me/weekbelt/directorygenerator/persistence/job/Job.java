package me.weekbelt.directorygenerator.persistence.job;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import me.weekbelt.directorygenerator.persistence.staffer.StafferJob;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Job extends BaseTimeEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer depth;

    @Column(nullable = false)
    private String branchId;

    @JsonManagedReference
    @Builder.Default
    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobSynonym> synonyms = new ArrayList<>();

    @JsonBackReference
    @Builder.Default
    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    private List<StafferJob> staffers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "job_type_id")
    private JobType jobType;

    public void addJobType(JobType jobType) {
        this.jobType = jobType;
        jobType.getJobs().add(this);
    }
}
