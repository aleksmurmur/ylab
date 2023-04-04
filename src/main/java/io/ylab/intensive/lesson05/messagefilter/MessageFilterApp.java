package io.ylab.intensive.lesson05.messagefilter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MessageFilterApp {
  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    applicationContext.registerShutdownHook();

    MessageLauncher launcher = applicationContext.getBean(MessageLauncher.class);
    launcher.start();
    //todo delete
//    MessageServiceImpl service = applicationContext.getBean(MessageServiceImpl.class);
//    System.out.println(service.replaceSwearwords("fuck fuck! fuck? fucker; fucking, fuck\nfucking\n!fucker! Fucking fucK afuck fucki"));
  }
}
