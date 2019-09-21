package com.group.activity;


import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;



@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityApplicationTests {

	    @Autowired
	    private RepositoryService repositoryService;
	    @Autowired
	    private RuntimeService runtimeService;
	    @Autowired
	    private TaskService taskService;

	    @Test
	    public void contextLoads() {    }

	    @Test
	    public void prepare() {
	        Deployment deployment = repositoryService.createDeployment()//创建一个部署对象
	                .name("流程")
	                .addClasspathResource("processes/NewProcess.bpmn")
	                .addClasspathResource("processes/NewProcess4.xml")
	                .deploy();
	       
	    
	        System.out.println("部署ID："+deployment.getId());
	        System.out.println("部署名称："+deployment.getName());
	    }

	    /**启动流程实例分配任务给个人*/
	    @Test
	    public void start() {

	        String userKey="xiaoming"; 
	        String processDefstainitionKey ="newProcess";//每一个流程有对应的一个key这个是某一个流程内固定的写在bpmn内的
	        HashMap<String, Object> variables=new HashMap<>();
	        variables.put("jingli", "jingliGroup");//userKey在上文的流程变量中指定了
	        // 流程变量必须先定义才能获取到
	        variables.put("user", "李经理");//userKey在上文的流程变量中指定了
	        // 默认使用最新的流程定义版本进行创建流程实例
	        ProcessInstance instance = runtimeService
	                .startProcessInstanceByKey(processDefstainitionKey,variables);
	        System.out.println("流程实例ID:"+instance.getId());
	        System.out.println("流程定义ID:"+instance.getProcessDefinitionId());

	    }

	    /**查询当前人的个人任务*/
	    @Test
	    public void findTask(){

	        String assignee = "李经理";
	        // 获取委派任务
	        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
	                .taskAssignee(assignee)//指定个人任务查询
	                .list();
	        // 获取任务流程变量
	        Map<String,Object> map1=  taskService.getVariables(list.get(0).getId());
	        for(String key :map1.keySet()) 
	        {
	        	Object valObject=map1.get(key);
	        	System.out.println("key："+key+"值 :"+valObject.toString());
	        }
	        
	        //String assignee1 = "李经理";
	        // 获取委派任务
	        List<Task> list2 = taskService.createTaskQuery()//创建任务查询对象
	                .taskCandidateOrAssigned("zjl")//指定个人任务查询
	                .list();
	        // 获取任务流程变量
	        Map<String,Object> map2=  taskService.getVariables(list2.get(0).getId());
	        
	        for(String key :map2.keySet()) 
	        {
	        	Object valObject=map1.get(key);
	        	System.out.println("key："+key+"值 :"+valObject.toString());
	        }
	        
	        
	        if(list!=null && list.size()>0)
	        {
	            for(Task task:list){
	                System.out.println("任务ID:"+task.getId());
	                System.out.println("任务名称:"+task.getName());
	                System.out.println("任务的创建时间:"+task.getCreateTime());
	                System.out.println("任务的办理人:"+task.getAssignee());
	                System.out.println("流程实例ID："+task.getProcessInstanceId());
	                System.out.println("执行对象ID:"+task.getExecutionId());
	                System.out.println("流程定义ID:"+task.getProcessDefinitionId());
	                System.out.println("getOwner:"+task.getOwner());
	                System.out.println("getCategory:"+task.getCategory());
	                System.out.println("getDescription:"+task.getDescription());
	                System.out.println("getFormKey:"+task.getFormKey());
	                Map<String, Object> map = task.getProcessVariables();
	                
	                for (Map.Entry<String, Object> m : map.entrySet()) {
	                    System.out.println("key:" + m.getKey() + " value:" + m.getValue());
	                }
	                
	                for (Map.Entry<String, Object> m : task.getTaskLocalVariables().entrySet()) {
	                    System.out.println("key:" + m.getKey() + " value:" + m.getValue());
	                }

	            }
	        }
	    }
	    
	    @Test
	    // 完成个人任务
	    public void completeTask1() throws ReflectiveOperationException, JSONException 
	    {
	    	String assignee = "李经理";
	        // 获取委派任务
	        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
	               .taskAssignee(assignee)//指定个人任务查询
	                .list();
	        
	        //taskService.complete(list.get(0).getId());
	   
	        //taskService.complete(list.get(1).getId());
	        // 完成任务
		/*
		 * for(Task task:list) { taskService.complete(task.getId()); }
		 */
	        
	        // 查询最新任务
	        List<Task> listx = taskService.createTaskQuery().taskAssignee("kermit")//创建任务查询对象
	                .list();
	    	taskService.setVariable(listx.get(0).getId(), "请假天数",3);
	        Person p = new Person();
			p.setId(1);
			p.setName("周江霄");
			p.setEducation("初中");
			taskService.setVariable(listx.get(0).getId(),"人员信息",(Object)p);
	
//	        List<Task> listx = taskService.createTaskQuery().taskCandidateGroup("management")//创建任务查询对象
//	                .list();
	        
//	        List<Task> listx = taskService.createTaskQuery().taskCandidateUser("zjl")//创建任务查询对象,无效有待验证
//	                .list();
	       int i=0;
	        
	        if(listx!=null && listx.size()>0)
	        {
	            for(Task task:listx){
	        	
	                System.out.println("任务ID:"+task.getId());
	                System.out.println("任务名称:"+task.getName());
	                System.out.println("任务的创建时间:"+task.getCreateTime());
	                System.out.println("任务的办理人:"+task.getAssignee());
	                System.out.println("流程实例ID："+task.getProcessInstanceId());
	                System.out.println("执行对象ID:"+task.getExecutionId());
	                System.out.println("流程定义ID:"+task.getProcessDefinitionId());
	                System.out.println("getOwner:"+task.getOwner());
	                System.out.println("getCategory:"+task.getCategory());
	                System.out.println("getDescription:"+task.getDescription());
	             
	               

	            }
	        }
	        
	       int day = (int)taskService.getVariable(listx.get(0).getId(),"请假天数");
	       System.out.println("getVar:"+day);
	       Object pp=  taskService.getVariable(listx.get(0).getId(), "人员信息");
	       
	       Person rp=new Person();
	       ConertObjToBean<Person> cb=new  ConertObjToBean<Person>(rp);
	       rp=cb.GetBean(pp);
	    
			System.out.println("getP"+rp.getId()+"  "+rp.getName());

	        
	    }
	    
	    
	
	    
	    
	   


	    
	    
}
