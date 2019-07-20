package com.jessin.practice;

import com.jessin.practice.model.Message;
import com.jessin.practice.model.Refuse;
import org.drools.core.command.runtime.BatchExecutionCommandImpl;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.command.CommandFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @create 2019-02-01 上午11:45
 **/
public class RefuseTest {

    @Test
    public void test1() {
        try {
            Map<String, String> refuseDate = new HashMap<String, String>();
            KieServices ks = KieServices.get();
            KieContainer kContainer = ks.getKieClasspathContainer();
            KieSession kSession = kContainer.newKieSession("ksession-rules");
            Message message = new Message();
            message.setStatus(Message.HELLO_WORLD);
            message.setDesc("老司机来了");
            //kSession.setGlobal("refuseDate", refuseDate);
            kSession.insert(message);
            int count = kSession.fireAllRules();
            System.out.println("规则执行条数：--------" + count);
            System.out.println("规则执行完成--------" + message);
            System.out.println(kSession.getGlobals().toString());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    @Test
    public void test2() {
        try {
            Map<String, String> refuseDate = new HashMap<String, String>();
            refuseDate.put("hello", "哈哈哈");
            Refuse refuse = new Refuse();
            refuse.setAge(80);
            KieServices ks = KieServices.get();
            KieContainer kContainer = ks.getKieClasspathContainer();
            KieSession kSession =  kContainer.getKieBase("base").newKieSession();
            BatchExecutionCommandImpl batchExecutionCommand = new BatchExecutionCommandImpl();
            batchExecutionCommand.addCommand(CommandFactory.newSetGlobal("refuseDate", refuseDate, true));
            batchExecutionCommand.addCommand(CommandFactory.newInsert(refuse, "refuse"));
            // 限定只执行一个规则，且必须有这个命令，才能执行规则
            batchExecutionCommand.addCommand(CommandFactory.newFireAllRules(1));
            ExecutionResults executionResults = kSession.execute(batchExecutionCommand);
            System.out.println("执行结果：--------" + executionResults.getValue("refuseDate"));
            System.out.println(refuse);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Test
    public void test3() {
        KieServices ks = KieServices.get();
        KieFileSystem kfs = ks.newKieFileSystem();
        ReleaseId rid = ks.newReleaseId("org.drools", "kiemodulemodel-example", "6.0.0-SNAPSHOT");
        kfs.generateAndWritePomXML(rid);

        // 构建一个kmodule.xml
        KieModuleModel kModuleModel = ks.newKieModuleModel();
        kModuleModel.newKieBaseModel("kiemodulemodel")
                .addPackage("rules")
                .newKieSessionModel("ksession6");
        String xml = kModuleModel.toXML();
        System.out.println("xml为：" + xml);
        // 产生kmodule.xml
        kfs.writeKModuleXML(xml);
        // 产生规则，将规则写到kfs中，HAL6为对应的路径，必须在上面指定的rules路径下才能找到
        kfs.write("src/main/resources/rules/HAL6.drl", getRule());
        // 基于kfs构建规则
        KieBuilder kb = ks.newKieBuilder(kfs);
        kb.buildAll();
        if (kb.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
        }
        KieContainer kContainer = ks.newKieContainer(rid);
        KieSession kSession = kContainer.newKieSession("ksession6");
        Refuse refuse = new Refuse();
        refuse.setAge(10);
        refuse.setWorkCity("北京");
        kSession.insert(refuse);
        kSession.fireAllRules();
    }

    private static String getRule() {
        String s = "" +
                "package org.drools.example.api.kiemodulemodel \n\n" +
                "import com.jessin.practice.model.Refuse \n\n" +
                "rule rule6 when \n" +
                "    refuse : Refuse(age == 10) \n" +
                "then\n" +
                "    insert( new Refuse() ); \n" +
                "    System.out.println(refuse);\n" +
                "end \n";
        return s;
    }


}
