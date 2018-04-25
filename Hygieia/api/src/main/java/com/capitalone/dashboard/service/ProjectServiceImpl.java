package com.capitalone.dashboard.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.model.Customer;
import com.capitalone.dashboard.model.Dashboard;
import com.capitalone.dashboard.model.HeatMap;
import com.capitalone.dashboard.model.Project;
import com.capitalone.dashboard.model.ProjectRoles;
import com.capitalone.dashboard.model.UserGroup;
import com.capitalone.dashboard.model.UserRole;
import com.capitalone.dashboard.repository.CustomerRepository;
import com.capitalone.dashboard.repository.DashboardRepository;
import com.capitalone.dashboard.repository.ProjectRepository;
import com.capitalone.dashboard.repository.UserRoleRepository;
import com.capitalone.dashboard.request.CustomerRequest;
import com.capitalone.dashboard.request.ProjectRequest;
import com.capitalone.dashboard.request.ProjectUserRoleRequest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final CustomerRepository customerRepository;
	private final UserRoleRepository userRoleRepository;
	private final DashboardRepository  dashboardRepository;
	private static final String SYS_ADMIN="SYS_ADMIN";
	private static final String TEAM_MEMBER="TEAM_MEMBER";
	@Autowired
	public ProjectServiceImpl(ProjectRepository projectRepository,UserRoleRepository userRoleRepository,DashboardRepository  dashboardRepository,
			CustomerRepository customerRepository) {
		this.projectRepository = projectRepository;
		this.userRoleRepository=userRoleRepository;
		this.dashboardRepository=dashboardRepository;
		this.customerRepository=customerRepository;
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
		
		project.setCustomerName(request.getCustomerName());
		project.setCustomerCode(request.getCustomerCode());
		
		project.setCreatedOn(new Date().toString());
		//setting the SYS_ADMIN role for the user who created the project.
		UserGroup defaultProjectAdmin= new UserGroup(request.getUser());
		ProjectRoles projRole= new ProjectRoles();
		projRole.setRole(SYS_ADMIN);

		if(userRoleRepository.findByRoleKey(projRole.getRole())!=null){
			projRole.setPermissions(userRoleRepository.findByRoleKey(projRole.getRole()).getPermissions().keySet());
			Set<ProjectRoles> projRoles = new HashSet<ProjectRoles>();
			projRoles.add(projRole);
			defaultProjectAdmin.setUserRoles(projRoles);
		}
		Set<UserGroup> userGroupSet= new HashSet<UserGroup>();
		userGroupSet.add(defaultProjectAdmin);
		project.setUsersGroup(userGroupSet);

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
		project.setCustomerName(request.getCustomerName());
		project.setCustomerCode(request.getCustomerCode());
		return project;
	}

	@Override
	public Project getProject(ObjectId id) {

		return projectRepository.findOne(id);
	}

	@Override
	public Iterable<Project> getProjectsOwnedByUser(String username) {
		return projectRepository.findByProjectUser(username,true);
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
				//projRole.setPermissions(role.getPermissions().keySet());
				//defect fix: ignoring the inactive permissions while adding the roles.
				projRole.setPermissions(role.getPermissions().entrySet().stream().filter(e -> e.getValue().booleanValue()==true).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).keySet());
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

			Set<Dashboard> dashboardsListToPersist=new HashSet<Dashboard>();
			Set<Dashboard> dashboardsListToRemove=new HashSet<Dashboard>();
			//edit existing dashboards if it is deferenced.
			List<Dashboard> existingProjectDashboards=dashboardRepository.findByProjectId(project.getId());
			List<Dashboard> dashboardsToAssignToTheUser=new ArrayList<Dashboard>(projectUserRoleRequest.getDashboardsToAssign());

			for(Dashboard dbDashboard: existingProjectDashboards){
				if(dashboardsToAssignToTheUser.contains(dbDashboard))
				{
					dbDashboard.getUsersList().add(projectUserRoleRequest.getUser());
					dashboardsListToPersist.add(dbDashboard);
				}else{
					dashboardsListToRemove.add(dbDashboard);
				}
			}

			for(Dashboard dbDashboard: dashboardsListToRemove)
			{
				dbDashboard.getUsersList().remove(projectUserRoleRequest.getUser());
				dashboardsListToPersist.add(dbDashboard);
			}

			dashboardRepository.save(dashboardsListToPersist);
			return projectRepository.save(project);
		}
		return null;
	}

	@Override
	public Iterable<Project> getActiveprojects() {
		return projectRepository.getAllActiveProjects(true);
	}

	@Override
	public Iterable<UserRole> getActiveprojectRolesOfUser( String projectId ,String user) {

		Set <String> userRoleKeys= new HashSet<String>();
		Set<UserRole> existingRoles= new HashSet<UserRole>();
		Project dbProject=projectRepository.getProject(projectId,user);
		if(null==dbProject){
			return null;
		}
		for(UserGroup usergrp :dbProject.getUsersGroup()){
			if(usergrp.getUser().equals(user)){
				for(ProjectRoles role:usergrp.getUserRoles()){
					userRoleKeys.add(role.getRole());
				}
			}
		}
		for(String key: userRoleKeys){
			existingRoles.add(userRoleRepository.findByRoleKey(key));
		}

		return existingRoles;
	}

	@Override
	public String disassociatedUserFromProject(String user, String projectId) {
		UserGroup userToPurge= new UserGroup(user);
		Project existingDBProject=projectRepository.getProject(projectId, user);
		existingDBProject.getUsersGroup().remove(userToPurge);
		projectRepository.save(existingDBProject);
		List<Dashboard> existingProjectDashboards=dashboardRepository.findByProjectId(existingDBProject.getId());
		for(Dashboard dbDashboard: existingProjectDashboards){
			dbDashboard.getUsersList().remove(user);
		}
		dashboardRepository.save(existingProjectDashboards);
		return "User has been disassociated successfully from the project";
	}

	@Override
	public String createGlobalDeliveryUser(String user) {
		List<Project> dbProjectsList=(List<Project>) projectRepository.getAllActiveProjects(true);
		UserGroup defaultProjectAccessUser= new UserGroup(user);
		ProjectRoles projRole= new ProjectRoles();
		projRole.setRole(TEAM_MEMBER);
		projRole.setPermissions(userRoleRepository.findByRoleKey(projRole.getRole()).getPermissions().keySet());
		Set<ProjectRoles> projRoles = new HashSet<ProjectRoles>();
		projRoles.add(projRole);
		defaultProjectAccessUser.setUserRoles(projRoles);
		int counter =0;
		List<Dashboard> existingProjectDashboards= new ArrayList<Dashboard>();
		List<Dashboard> dashboardsToSave= new ArrayList<Dashboard>();
		for(Project project: dbProjectsList){
			project.getUsersGroup().add(defaultProjectAccessUser);
			existingProjectDashboards=dashboardRepository.findByProjectId(project.getId());
			for(Dashboard dbDashboard: existingProjectDashboards){
				dbDashboard.getUsersList().add(user);
				dashboardsToSave.add(dbDashboard);
			}
			counter++;
		}

		projectRepository.save(dbProjectsList);
		dashboardRepository.save(dashboardsToSave);
		return "Access Granted for "+counter+" Projects";
	}

	@Override
	public String createAdditionalSysAdmins(String user) {
		List<Project> dbProjectsList=(List<Project>) projectRepository.getAllActiveProjects(true);
		UserGroup defaultProjectAccessUser= new UserGroup(user);
		ProjectRoles projRole= new ProjectRoles();
		projRole.setRole(SYS_ADMIN);
		projRole.setPermissions(userRoleRepository.findByRoleKey(projRole.getRole()).getPermissions().keySet());
		Set<ProjectRoles> projRoles = new HashSet<ProjectRoles>();
		projRoles.add(projRole);
		defaultProjectAccessUser.setUserRoles(projRoles);
		int counter =0;
		List<Dashboard> existingProjectDashboards= new ArrayList<Dashboard>();
		List<Dashboard> dashboardsToSave= new ArrayList<Dashboard>();
		for(Project project: dbProjectsList){
			project.getUsersGroup().add(defaultProjectAccessUser);
			existingProjectDashboards=dashboardRepository.findByProjectId(project.getId());
			for(Dashboard dbDashboard: existingProjectDashboards){
				dbDashboard.getUsersList().add(user);
				dashboardsToSave.add(dbDashboard);
			}
			counter++;
		}

		projectRepository.save(dbProjectsList);
		dashboardRepository.save(dashboardsToSave);
		return "Access Granted for "+counter+" Projects";
	}

	@Override
	public String purgeUser(String user) {
		try{
			UserGroup userToPurge= new UserGroup(user);
			List<Project> userAccessibleProjects= (List<Project>) projectRepository.findByProjectUser(user, true);
			List<Dashboard> existingProjectDashboards= new ArrayList<Dashboard>();
			List<Dashboard> dashboardsToSave= new ArrayList<Dashboard>();
			int counter=0;
			for( Project project: userAccessibleProjects){
				if(null!=project.getUsersGroup()){
					project.getUsersGroup().remove(userToPurge);
					existingProjectDashboards=dashboardRepository.findByProjectId(project.getId());
					for(Dashboard dbDashboard: existingProjectDashboards){
						dbDashboard.getUsersList().remove(user);
						dashboardsToSave.add(dbDashboard);
					}
				}
				counter++;
			}
			projectRepository.save(userAccessibleProjects);
			dashboardRepository.save(dashboardsToSave);
			return "User has been disassociated from "+counter+" project(s)";
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public void createCustomer() {
		List<Customer> customers = mapToCSV();
		for(Customer customer : customers){
			customerRepository.save(customer);
		}
	}


	private List<Customer> mapToCSV() {
		String path = "E:\\CSV\\custom.csv";

		List<Customer> customers = new ArrayList<>();
		try {

			path = path.replace("\\", "/");

			BufferedReader br = new BufferedReader(new FileReader(path));

			String line;
			while ((line = br.readLine()) != null) {

				String[] entries = line.split(",");

				Customer customer = createCustomer(entries);

				customers.add(customer);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return customers;
	}

	private  Customer createCustomer(String[] metadata){
		String customer_name = metadata[0];
		String customerCode = metadata[1];
		String deactivated = metadata[2];

		Customer customer = customerRepository.findByCustomerCode(customerCode);
		if (null == customer) {
			customer = new Customer();
			customer.setCustomerCode(customerCode);
		}
		customer.setCustomer_name(customer_name);
		customer.setDeactivated(deactivated);
		return customer;
	}

	@Override
	public List<Customer> getCustomer() {
		List<Customer> customers = (List<Customer>) customerRepository.findAll();
		return customers;
	}

	@Override
	public Customer createCustomers(CustomerRequest customerRequest) {
		return customerRepository.save(mapCustomer(customerRequest));
	}

	private Customer mapCustomer(CustomerRequest customerRequest) {
		Customer customer = customerRepository
				.findByCustomerCode(customerRequest.getCustomerCode());
		if (null == customer) {
			customer = new Customer();
			customer.setCustomerCode(customerRequest.getCustomerCode());
		}
		customer.setCustomer_name(customerRequest.getCustomerName());
		customer.setDeactivated(customerRequest.getDeactivate());
		return customer;
	}
}
