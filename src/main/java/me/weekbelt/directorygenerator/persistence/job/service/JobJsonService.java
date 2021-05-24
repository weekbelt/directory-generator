package me.weekbelt.directorygenerator.persistence.job.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.weekbelt.directorygenerator.persistence.job.Job;
import me.weekbelt.directorygenerator.persistence.job.JobConverter;
import me.weekbelt.directorygenerator.persistence.job.JobType;
import me.weekbelt.directorygenerator.persistence.job.NewJobJson;
import me.weekbelt.directorygenerator.persistence.job.repository.JobRepository;
import me.weekbelt.directorygenerator.persistence.job.repository.JobTypeRepository;
import me.weekbelt.directorygenerator.persistence.common.fileUtil.JsonExporter;
import me.weekbelt.directorygenerator.persistence.staffer.OldStafferJson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JobJsonService {

    private final JsonExporter jsonExporter;
    private final JobRepository jobRepository;
    private final JobTypeRepository jobTypeRepository;

    public Resource generateNewJob(String branchName) throws IOException {
        List<OldStafferJson> oldStafferJsons = jsonExporter.getOldStafferJsons(branchName);
        String branchId = getBranchId(oldStafferJsons);

        JobType jobType = saveJobType(branchId);
        saveJobs(oldStafferJsons, jobType);
        List<NewJobJson> newJobJsonList = getNewJobJsonList();
        return jsonExporter.getByteArrayResource(newJobJsonList);
    }

    private String getBranchId(List<OldStafferJson> oldStafferJsons) {
        OldStafferJson oldStafferJson = oldStafferJsons.get(0);
        return oldStafferJson.getBchID();
    }

    private JobType saveJobType(String branchId) {
        return jobTypeRepository.save(JobType.builder()
            .id(UUID.randomUUID().toString()).name("DEFAULT").branchId(branchId)
            .build());
    }

    private void saveJobs(List<OldStafferJson> stafferJsonList, JobType jobType) {
        stafferJsonList.forEach(stafferJson -> {
            stafferJson.getJobs().forEach(jobName -> {
                saveJobIfNotExists(stafferJson, jobName, jobType);
            });
        });
    }

    private void saveJobIfNotExists(OldStafferJson stafferJson, String jobName, JobType jobType) {
        String trimJob = jobName.trim();
        if (!jobRepository.existsByName(trimJob) && StringUtils.isNotBlank(trimJob)) {
            Job job = JobConverter.convertToJob(stafferJson, trimJob);
            job.addJobType(jobType);
            jobRepository.save(job);
        }
    }

    private List<NewJobJson> getNewJobJsonList() {
        List<Job> jobList = jobRepository.findAll();
        return jobList.stream().map(JobConverter::convertToNewJobJsonFromJob).collect(Collectors.toList());
    }
}
