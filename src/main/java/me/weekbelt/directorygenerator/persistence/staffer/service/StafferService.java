package me.weekbelt.directorygenerator.persistence.staffer.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.weekbelt.directorygenerator.persistence.common.PhoneType;
import me.weekbelt.directorygenerator.persistence.common.fileUtil.JsonExporter;
import me.weekbelt.directorygenerator.persistence.department.Department;
import me.weekbelt.directorygenerator.persistence.department.repository.DepartmentRepository;
import me.weekbelt.directorygenerator.persistence.job.Job;
import me.weekbelt.directorygenerator.persistence.job.JobType;
import me.weekbelt.directorygenerator.persistence.job.repository.JobRepository;
import me.weekbelt.directorygenerator.persistence.job.repository.JobTypeRepository;
import me.weekbelt.directorygenerator.persistence.position.Position;
import me.weekbelt.directorygenerator.persistence.position.repository.PositionRepository;
import me.weekbelt.directorygenerator.persistence.staffer.NewStafferJson;
import me.weekbelt.directorygenerator.persistence.staffer.Staffer;
import me.weekbelt.directorygenerator.persistence.staffer.StafferJob;
import me.weekbelt.directorygenerator.persistence.staffer.StafferPosition;
import me.weekbelt.directorygenerator.persistence.staffer.repository.StafferJobRepository;
import me.weekbelt.directorygenerator.persistence.staffer.repository.StafferPositionRepository;
import me.weekbelt.directorygenerator.persistence.staffer.repository.StafferRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class StafferService {

    private final JsonExporter jsonExporter;
    private final JobTypeRepository jobTypeRepository;
    private final JobRepository jobRepository;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final StafferRepository stafferRepository;
    private final StafferJobRepository stafferJobRepository;
    private final StafferPositionRepository stafferPositionRepository;

    public void saveAllStaffersAndJobsAndPositions(String branchName) {
        List<NewStafferJson> newStafferJsons = jsonExporter.getNewStafferJsons(branchName);
        String branchId = newStafferJsons.get(0).getBranchId();
        /**
         * 1. JobType, Job 저장
         * 2. Position 저장
         * 3. Staffer 저장
         * 4. StafferJob, StafferPosition 저장
         */
        // 1. JobType 저장
        JobType jobType = jobTypeRepository.save(JobType.builder().id(UUID.randomUUID().toString()).name("DEFAULT").branchId(branchId).build());

        for (NewStafferJson newStafferJson : newStafferJsons) {
            List<Job> jobs = saveJobs(branchId, newStafferJson, jobType);
            List<Position> positions = savePositions(branchId, newStafferJson);
            Staffer staffer = saveStaffer(branchId, newStafferJson);

            saveStafferJobs(branchId, jobs, staffer);
            saveStafferPositions(branchId, positions, staffer);
        }
    }

    private void saveStafferPositions(String branchId, List<Position> positions, Staffer staffer) {
        for (Position position : positions) {
            StafferPosition stafferPosition = StafferPosition.builder()
                .id(UUID.randomUUID().toString())
                .staffer(staffer)
                .position(position)
                .branchId(branchId)
                .build();
            stafferPositionRepository.save(stafferPosition);
        }
    }

    private void saveStafferJobs(String branchId, List<Job> jobs, Staffer staffer) {
        for (Job job : jobs) {
            StafferJob stafferJob = StafferJob.builder()
                .id(UUID.randomUUID().toString())
                .staffer(staffer)
                .job(job)
                .branchId(branchId)
                .build();
            stafferJobRepository.save(stafferJob);
        }
    }

    private Staffer saveStaffer(String branchId, NewStafferJson newStafferJson) {
        Staffer staffer = Staffer.builder()
            .id(newStafferJson.getId())
            .name(newStafferJson.getName())
            .phoneNumber(newStafferJson.getStafferPhone().getNumber())
            .phoneType(PhoneType.valueOf(newStafferJson.getStafferPhone().getType()))
            .areas(String.join(", ", newStafferJson.getAreas()))
            .branchId(branchId)
            .build();

        Department department = departmentRepository.findById(newStafferJson.getDepartmentId())
            .orElseThrow(() -> new EntityNotFoundException("존재 하지 않는 Department입니다. departmentId=" + newStafferJson.getDepartmentId()));
        staffer.addDepartment(department);

        return stafferRepository.save(staffer);
    }

    private List<Job> saveJobs(String branchId, NewStafferJson newStafferJson, JobType jobType) {
        List<String> jobs = newStafferJson.getJobs();
        return jobs.stream()
            .filter(jobName -> !jobName.equals(""))
            .map(jobName -> {
                if (!jobRepository.existsByName(jobName)) {
                    Job job = Job.builder()
                        .id(UUID.randomUUID().toString())
                        .name(jobName)
                        .depth(1)
                        .branchId(branchId)
                        .jobType(jobType)
                        .build();
                    return jobRepository.save(job);
                } else {
                    return jobRepository.findByName(jobName)
                        .orElseThrow(() -> new EntityNotFoundException("존재 하지 않는 Job입니다. jobName=" + jobName));
                }
            }).collect(Collectors.toList());
    }

    private List<Position> savePositions(String branchId, NewStafferJson newStafferJson) {
        List<String> positions = newStafferJson.getPositions();
        return positions.stream()
            .filter(positionName -> !positionName.equals(""))
            .map(positionName -> {
                if (!positionRepository.existsByName(positionName)) {
                    Position position = Position.builder()
                        .id(UUID.randomUUID().toString())
                        .name(positionName)
                        .branchId(branchId)
                        .build();
                    return positionRepository.save(position);
                } else {
                    return positionRepository.findByName(positionName)
                        .orElseThrow(() -> new EntityNotFoundException("존재 하지 않는 Position입니다. positionName=" + positionName));
                }
            }).collect(Collectors.toList());
    }

}
