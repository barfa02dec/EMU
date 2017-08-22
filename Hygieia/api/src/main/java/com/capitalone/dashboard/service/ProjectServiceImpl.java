package com.capitalone.dashboard.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.model.Project;
import com.capitalone.dashboard.model.ProjectRoles;
import com.capitalone.dashboard.model.UserGroup;
import com.capitalone.dashboard.model.UserRole;
import com.capitalone.dashboard.repository.ProjectRepository;
import com.capitalone.dashboard.repository.UserRoleRepository;
import com.capitalone.dashboard.request.ProjectRequest;
import com.capitalone.dashboard.request.ProjectUserRoleRequest;

@Service
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final UserRoleRepository userRoleRepository;
	@Autowired
	public ProjectServiceImpl(ProjectRepository projectRepository,UserRoleRepository userRoleRepository) {
		this.projectRepository = projectRepository;
		this.userRoleRepository=userRoleRepository;
	}

	/*@Override
	public Iterable<Project> all() {

		return projectRepository.findAll(new Sort(Sort.Direction.ASC, "projectName"));
	}*/

	@Override
	public Project create(ProjectRequest projectRequest) {
		Project project=new Project();
		mapProjectRequestToPojectForCreateProject(projectRequest, project);
		return projectRepository.save(project);
		
	}

	@Override
	public Project deactivateProject(ObjectId id) {
			
			Project project = projectRepository.findOne(id);
			if (project != null) {
				project.setProjectStatus(false);
				return projectRepository.save(project);
				
			}else
			{
				return null;
			}

	}

	@Override
	public Project updateProject(ProjectRequest projectRequest) {

			try{
				
				Project project=projectRepository.findOne(new ObjectId(projectRequest.getId()));
				if(null!=project)
				{
					mapProjectRequestToPojectForUpdateProject(projectRequest, project);
					return projectRepository.save(project);

				}
				
			}catch (Exception e) {
				return null;
			}
			return null;
		
	}
	
	private Project mapProjectRequestToPojectForCreateProject(ProjectRequest request, Project project ){
		project.setProjectId(request.getProjectId());
		project.setProjectName(request.getProjectName());
		project.setProjectOwner(request.getProjectOwner());
		project.setProjectStatus(true);
		project.setClient(request.getClient());
		project.setBusinessUnit(request.getBusinessUnit());
		project.setProgram(request.getProgram());
		project.setCreatedBy(request.getUser());
		project.setCreatedOn(new Date().toString());
		//setting the SYS_ADMIN role for the user who created the project.
		UserGroup defaultProjectAdmin= new UserGroup(request.getUser());
		ProjectRoles projRole= new ProjectRoles();
		projRole.setRole("SYS_ADMIN");
		
		if(userRoleRepository.findByRoleKey(projRole.getRole())!=null){
			projRole.setPermissions(userRoleRepository.findByRoleKey(projRole.getRole()).getPermissions().keySet());
			Set<ProjectRoles> projRoles = new HashSet<ProjectRoles>();
			projRoles.add(projRole);
			defaultProjectAdmin.setUserRoles(projRoles);
			Set<UserGroup> userGroupSet= new HashSet<UserGroup>();
			userGroupSet.add(defaultProjectAdmin);
			project.setUsersGroup(userGroupSet);
		}
		
		return project;
	}
	
	private Project mapProjectRequestToPojectForUpdateProject(ProjectRequest request, Project project ){
		project.setProjectName(request.getProjectName());
		project.setProjectOwner(request.getProjectOwner());
		project.setClient(request.getClient());
		project.setBusinessUnit(request.getBusinessUnit());
		project.setProgram(request.getProgram());
		project.setUpdatedBy(request.getUser());
		project.setUpdatedOn(new Date().toString());
		return project;
	}
	
	@Override
	public Project getProject(ObjectId id) {
		
		return projectRepository.findOne(id);
	}

	@Override
	public Iterable<Project> getProjectsOwnedByUser(String username) {
		return projectRepository.findByProjectUser(username);
	}

	@Override
	public Project createProjectUserRoleMapping(ProjectUserRoleRequest projectUserRoleRequest) {
		Project project=projectRepository.findByProjectId(projectUserRoleRequest.getProjectId());
		if(null!=project){
			UserGroup usergrpToCreate= new UserGroup(projectUserRoleRequest.getUser());
			//prepare roles 
			Set<ProjectRoles> projRoles = new HashSet<ProjectRoles>();
			for(UserRole role: projectUserRoleRequest.getUserRoles()){
				ProjectRoles projRole= new ProjectRoles();
				projRole.setRole(role.getRoleKey());
				projRole.setPermissions(role.getPermissions().keySet());
				projRoles.add(projRole);
			}
			//check if user already exists in group, then reset the roles, else create new user in the group with roles.
			if(null!=project.getUsersGroup()){
				if(project.getUsersGroup().contains(usergrpToCreate)){
					for(UserGroup group:project.getUsersGroup()){
						if(group.getUser().equals(usergrpToCreate.getUser())){
							group.setUserRoles(projRoles);
							break;
						}
					}
				}else{
					usergrpToCreate.setUserRoles(projRoles);
					project.getUsersGroup().add(usergrpToCreate);
				}
					
			}else{
				Set<UserGroup> userGroupSet= new HashSet<UserGroup>();
				usergrpToCreate.setUserRoles(projRoles);
				userGroupSet.add(usergrpToCreate);
				project.setUsersGroup(userGroupSet);
			}
			
		
			return projectRepository.save(project);
		}
		return null;
	}

	@Override
	public Iterable<Project> getActiveprojects() {
		return projectRepository.getAllActiveProjects(true);
	}

}
