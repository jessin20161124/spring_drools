package com.jessin.practice;

import com.jessin.practice.model.Refuse;
import org.drools.core.command.runtime.BatchExecutionCommandImpl;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
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
            KieSession kSession = kContainer.newKieSession("session-base");
            Refuse refuse = new Refuse();
            refuse.setAge(80);
            kSession.setGlobal("refuseDate", refuseDate);
            kSession.insert(refuse);
            int count = kSession.fireAllRules();
            System.out.println("规则执行条数：--------" + count);
            System.out.println("规则执行完成--------" + refuse);
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

//        Resource ex1Res = ks.getResources().newFileSystemResource(getFile("named-kiesession"));
//        Resource ex2Res = ks.getResources().newFileSystemResource(getFile("kiebase-inclusion"));

        ReleaseId rid = ks.newReleaseId("org.drools", "kiemodulemodel-example", "6.0.0-SNAPSHOT");
        kfs.generateAndWritePomXML(rid);

        KieModuleModel kModuleModel = ks.newKieModuleModel();
        kModuleModel.newKieBaseModel("kiemodulemodel")
//                .addInclude("kiebase1")
//                .addInclude("kiebase2")
                .newKieSessionModel("ksession6");

        kfs.writeKModuleXML(kModuleModel.toXML());
        kfs.write("src/main/resources/kiemodulemodel/HAL6.drl", getRule());
        KieBuilder kb = ks.newKieBuilder(kfs);
//        kb.setDependencies(ex1Res, ex2Res);
        kb.buildAll(); // kieModule is automatically deployed to KieRepository if successfully built.
        if (kb.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
        }
        KieContainer kContainer = ks.newKieContainer(rid);
        KieSession kSession = kContainer.newKieSession("ksession6");
        Refuse refuse = new Refuse();
        refuse.setAge(10);
        kSession.insert(refuse);
        kSession.fireAllRules();
    }

    private static String getRule() {
        String s = "" +
                "package org.drools.example.api.kiemodulemodel \n\n" +
                "import com.jessin.practice.model.Refuse \n\n" +
                "rule rule6 when \n" +
                "    Refuse(age == 10) \n" +
                "then\n" +
                "    insert( new Refuse() ); \n" +
                "    System.out.println(\"hello\");\n" +
                "end \n";
        return s;
    }


}
