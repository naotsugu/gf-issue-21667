package code.javaee.payara;

import org.glassfish.embeddable.*;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Logger;

public class EmbeddedMain {

    private static final Logger logger = Logger.getLogger(EmbeddedMain.class.getName());

    public static void main(String [] args) throws Exception {

        Locale.setDefault(Locale.US);
        System.setProperty("user.country","US");
        System.setProperty("user.language","en");

        org.h2.tools.Server.createWebServer().start();
        org.h2.tools.Server.createTcpServer("-tcpAllowOthers").start();

        BootstrapProperties bootstrapProperties = new BootstrapProperties();
        GlassFishRuntime runtime = GlassFishRuntime.bootstrap(bootstrapProperties);

        GlassFishProperties glassfishProperties = new GlassFishProperties();
        glassfishProperties.setPort("http-listener", 9090);
        GlassFish glassfish = runtime.newGlassFish(glassfishProperties);

        configureSecurity();

        glassfish.start();

        CommandResult result = glassfish.getCommandRunner().run(
                "create-auth-realm",
                "--classname", "com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm",
                "--property", "jaas-context=jdbcRealm"
                        + ":datasource-jndi=\"java:app/primaryDS\""
                        + ":user-table=user"
                        + ":user-name-column=username"
                        + ":password-column=password"
                        + ":group-table=authgroup_user"
                        + ":group-name-column=groupname"
                        + ":digest-algorithm=SHA-256",
                "appJdbcRealm");

        logger.info(glassfish.getCommandRunner().run("list-auth-realms").getOutput());

        File war = (args != null && args.length > 0)
                ? new File(args[0])
                : new File("app/build/libs/app.war");
        Deployer deployer = glassfish.getDeployer();
        deployer.deploy(war);

        browse("http://localhost:9090/app/rs/");
    }


    public static void browse(String uri) throws Exception {
        Desktop.getDesktop().browse(new URI(uri));
    }


    private static void configureSecurity() {

        String instanceRootStr = System.getProperty("com.sun.aas.instanceRoot");
        File configDir = new File(instanceRootStr, "config");

        if (System.getProperty("java.security.auth.login.config") == null) {
            System.setProperty("java.security.auth.login.config",
                    new File(configDir.getAbsolutePath(), "login.conf").getAbsolutePath());
        }

        if (System.getProperty("java.security.policy") == null) {
            System.setProperty("java.security.policy",
                    new File(configDir.getAbsolutePath(), "server.policy").getAbsolutePath());
        }

        if (System.getProperty("javax.net.ssl.keyStore") == null) {
            System.setProperty("javax.net.ssl.keyStore",
                    new File(configDir.getAbsolutePath(),"keystore.jks").getAbsolutePath());
        }

        if (System.getProperty("javax.net.ssl.trustStore") == null) {
            System.setProperty("javax.net.ssl.trustStore",
                    new File(configDir.getAbsolutePath(),"cacerts.jks").getAbsolutePath());
        }
    }
}
