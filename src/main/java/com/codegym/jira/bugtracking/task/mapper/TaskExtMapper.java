package com.codegym.jira.bugtracking.task.mapper;

import com.codegym.jira.common.BaseMapper;
import com.codegym.jira.common.TimestampMapper;
import com.codegym.jira.common.error.DataConflictException;
import com.codegym.jira.login.AuthUser;
import com.codegym.jira.bugtracking.project.ProjectMapper;
import com.codegym.jira.bugtracking.sprint.SprintMapper;
import com.codegym.jira.bugtracking.task.Task;
import com.codegym.jira.bugtracking.task.to.TaskToExt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = TimestampMapper.class, uses = {SprintMapper.class, ProjectMapper.class})
public interface TaskExtMapper extends BaseMapper<Task, TaskToExt> {

    static long checkProjectBelong(long projectId, Task dbTask) {
        if (projectId != dbTask.getProjectId())
            throw new DataConflictException("Task " + dbTask.id() + " doesn't belong to Project " + projectId);
        return projectId;
    }

    static long checkUserAuthorities(long sprintId, Task dbTask) {
        if (sprintId != dbTask.getSprintId() && !(AuthUser.get().isAdmin() || AuthUser.get().isManager()))
            throw new DataConflictException("Do not have authorities to change task's sprint");
        return sprintId;
    }

    @Override
    TaskToExt toTo(Task task);

    @Override
    @Mapping(target = "projectId", expression = "java(TaskExtMapper.checkProjectBelong(taskToExt.getProjectId(), task))")
    @Mapping(target = "sprintId", expression = "java(TaskExtMapper.checkUserAuthorities(taskToExt.getSprintId(), task))")
    Task updateFromTo(TaskToExt taskToExt, @MappingTarget Task task);
}
