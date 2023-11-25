package com.atguigu.process.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.process.Process;
import com.atguigu.model.process.ProcessTemplate;
import com.atguigu.model.system.SysUser;
import com.atguigu.process.service.MessageService;
import com.atguigu.process.service.OaProcessService;
import com.atguigu.process.service.OaProcessTemplateService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private OaProcessService processService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OaProcessTemplateService processTemplateService;

    @Autowired
    private WxMpService wxMpService;

    /**
     * 推送待审批人员
     * @param processId 流程id
     * @param userId 要推送给谁的id
     * @param taskId 任务id
     */
    @Override
    public void pushPendingMessage(Long processId, Long userId, String taskId) {

        //查询流程信息
        Process process = processService.getById(processId);
        //根据userId查询要推送人的信息
        SysUser sysUser = sysUserService.getById(userId);
        //查询审批模板信息
        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());
        //获取提交审批人的信息
        SysUser submitSysUser = sysUserService.getById(process.getUserId());

        //获取要给的消息人的openid
        String openId = sysUser.getOpenId();
        if (StringUtils.isEmpty(openId)){
            //TODO 为了测试
            openId="o95ty6YasSGMZVSuXyhdot-ZEO-A";
        }
        //设置消息发送信息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                //给谁发送消息，openId值
                .toUser(openId)
                //公众号平台里设置的模板id
                .templateId("W4gjWxB195ijxuX3xdsv8KoeABUxWCV3Kfz8B1Iy3e0")
                //点击消息，跳转的地址
                .url("http://oafjf2.v2.idcfengye.com/#/show/" + processId + "/" + taskId)
                .build();

        //获取 process表中的FormValues数据

        JSONObject jsonObject = JSON.parseObject(process.getFormValues());
        JSONObject formShowData = jsonObject.getJSONObject("formShowData");
        System.out.println(formShowData);
        StringBuffer content = new StringBuffer();
        for (Map.Entry entry : formShowData.entrySet()) {
            content.append(entry.getKey()).append("：").append(entry.getValue()).append("\n ");
        }
        System.out.println(content);

        //设置模板里面参数值
        templateMessage.addData(new WxMpTemplateData("first", submitSysUser.getName()+"提交"+processTemplate.getName()+"审批申请，请注意查看。", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword1", process.getProcessCode(), "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword2", new DateTime(process.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"), "#272727"));
        templateMessage.addData(new WxMpTemplateData("content", content.toString(), "#272727"));

        //调用方法发送
        try {
            String msg = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
}
