package sorcer.assignment.junit;

import org.junit.Test;
import sorcer.assignment.TextService;
import sorcer.core.SorcerConstants;
import sorcer.service.ServiceExertion;
import sorcer.service.Task;
import sorcer.util.Sorcer;

import java.rmi.RMISecurityManager;
import java.util.logging.Logger;

import static sorcer.eo.operator.*;
import static sorcer.eo.operator.value;

/**
 * Created by piotrjedruszuk on 17.01.2014.
 */
public class TextServiceTest implements SorcerConstants {

    private final static Logger logger = Logger.getLogger(TextServiceTest.class.getName());

    static {
        ServiceExertion.debug = true;
        System.setProperty("java.security.policy", Sorcer.getHome()
                + "/configs/policy.all");
        System.setSecurityManager(new RMISecurityManager());
        Sorcer.setCodeBase(new String[] { "jeri-assignment-dl.jar" });
        System.out.println("CLASSPATH :" + System.getProperty("java.class.path"));
        System.setProperty("java.protocol.handler.pkgs", "sorcer.util.url|org.rioproject.url");
    }

    @Test
    public void spellcheckerTest() throws Exception {
        Task t1 = task("t1",
                sig("correctText", TextService.class, "Spellchecker"),
                context("text", result("text")));

        logger.info("t1 value: " + value(t1));

    }

    @Test
    public void duplicateDeleter() throws Exception {
        Task t2 = task("t2",
                sig("correctText", TextService.class, "DuplicateDeleter"),
                context("text", in("text", "Hello,, hello... hello.... Hello hEllo \\n He1llo hE1llo heLlo World;::, hello."), result("text")));


        logger.info("t2 value: " + value(t2));
    }


}
