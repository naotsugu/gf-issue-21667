package code.javaee.starter;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import java.util.logging.Logger;

@Stateless
@LocalBean
public class Service {
    private static final Logger logger = Logger.getLogger(Service.class.getName());

    @Resource
    private SessionContext context;

    @Asynchronous
    public void exec() {
        logger.info("exec()[" + context.getCallerPrincipal() + "]:" + Thread.currentThread().getName());
    }
}
