package com.atguigu.auth.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SpringBootTest
public class ProcessTestDemo02 {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Test
    public void deployProcess(){
        //流程部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban.bpmn20.xml")
                .name("加班申请流程")
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());

    }

    //启动实例
    @Test
    public void startProcessInstance(){
        Map<String,Object> map = new HashMap<>();
        //设置任务人
        map.put("assignee1", "lucy03"); //启动流程时设置变量
        //map.put("assignee2", "mary03");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban", map);
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }

    @Test
    public void completTask() {
        Task task = taskService.createTaskQuery()
                .taskAssignee("lucy03")  //要查询的负责人
                .singleResult();//返回一条

        Map<String, Object> variables = new HashMap<>();
        variables.put("assignee2", "mary03"); //在任务办理时设置流程变量
        //完成任务,参数：任务id
        taskService.complete(task.getId(), variables);
    }


    //查询个人的代办任务-mary03
    @Test
    public void findTaskList(){
        //任务负责人
        String assign = "mary03";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assign).list();//只查询该任务负责人的任务
        for (Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }
}
