package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.TaskMember;
import com.protrack.protrack_be.model.id.TaskMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskMemberRepository extends JpaRepository<TaskMember, TaskMemberId> {}
