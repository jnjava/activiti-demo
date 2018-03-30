package org.activiti.designer.test;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestMyProcess {

	private String filename = "D:\\WorkSpace-java-git\\ds-task\\ds-task-basic-1\\src\\main\\resources\\workflowDef\\basicflow.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("myProcess.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
	}
	
	@Test
	public void findTask(){
		TaskService taskservice = activitiRule.getTaskService();
		List<Task> tasklist = taskservice.createTaskQuery().taskAssignee("user:0001").list();
		for(Task task:tasklist){
			System.out.println("任务ID:"+task.getId());
			System.out.println("任务名称:"+task.getName());
			System.out.println("任务创建时间:"+task.getCreateTime());
			System.out.println("任务执行人:"+task.getAssignee());
			System.out.println("任务实例ID:"+task.getProcessInstanceId());
		}
		
	}
	
	@Test
	public void ClaimTask()
	{
		TaskService taskservice = activitiRule.getTaskService();
		List<Task> tasklist = taskservice.createTaskQuery().taskAssignee("user:0001").list();
		for(Task task:tasklist){
			System.out.println("任务ID:"+task.getId());
			System.out.println("任务名称:"+task.getName());
			System.out.println("任务创建时间:"+task.getCreateTime());
			System.out.println("任务执行人:"+task.getAssignee());
			System.out.println("任务实例ID:"+task.getProcessInstanceId());
			
			taskservice.claim(task.getId(), "user:0001");
			
			
		}
		
		
	}
	
	@Test
	public void ExecuteTask()
	{
		IdentityService identityService = activitiRule.getIdentityService();
		identityService.saveGroup(new GroupEntity("部门经理"));//建立组 
		identityService.saveGroup(new GroupEntity("总经理"));//建立组 
		identityService.saveUser(new UserEntity("小张"));//建立用户 
		identityService.saveUser(new UserEntity("小李")); //建立用户 
		identityService.saveUser(new UserEntity("小王")); //建立用户 
		identityService.createMembership("小张", "部门经理");//建立组和用户关系 
		identityService.createMembership("小李", "部门经理");//建立组和用户关系 
		identityService.createMembership("小王", "总经理");//建立组和用户关系 
	}
}