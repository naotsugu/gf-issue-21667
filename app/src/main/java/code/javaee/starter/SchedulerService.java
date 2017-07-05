package code.javaee.starter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import java.util.logging.Logger;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class SchedulerService {
    private static final Logger logger = Logger.getLogger(SchedulerService.class.getName());

    @Resource
    private TimerService timerService;

    @Resource
    private SessionContext context;

    @PostConstruct
    public void postConstruct() {
        TimerConfig timerConfig = new TimerConfig();
        // ...
        timerService.createIntervalTimer(0, 1000, timerConfig);
    }

    @Timeout
    public void handleTimeout(Timer timer) {
        logger.info("handleTimeout()[" + context.getCallerPrincipal() + "]:" + Thread.currentThread().getName());
    }
}
