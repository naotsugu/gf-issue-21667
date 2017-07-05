package code.javaee.starter;

import javax.annotation.*;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@DataSourceDefinition(
        name = "java:app/primaryDS",
        className = "org.h2.jdbcx.JdbcDataSource",
        url = "jdbc:h2:tcp://localhost/./db;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1",
        user = "sa"
)
@Singleton
@Startup
public class DataSourceInitializer {

    @PersistenceContext(unitName = "primaryPU")
    private EntityManager em;

    @Resource(lookup = "java:app/primaryDS")
    private DataSource dataSource;

    @PostConstruct
    public void init() {
        List<AuthGroup> groups = new ArrayList<>();
        User user = new User("user1", hash("user1"), groups);

        em.persist(user);

        List<User> users = new ArrayList<>();
        users.add(user);
        AuthGroup group = new AuthGroup("group1", users);
        em.persist(group);

        groups.add(group);
    }

    private String hash(String plainPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(plainPassword.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private final static char[] hexArray = "0123456789abcdef".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
