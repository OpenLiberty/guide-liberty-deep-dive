package io.openliberty.guides;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Connection;


@ApplicationScoped
public class JdbcDataSourceVerifier {

    @Resource(lookup = "jdbc/postgresql")
    private DataSource dataSource;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) throws SQLException {
        System.out.println("Verifying connection to PostgreSQL");    
        final String user = "admin";
        final String password = "adminpwd"; 
        System.out.println("before");
        Connection connection = dataSource.getConnection(user, password);
        System.out.println("after");
        System.out.println(connection.getMetaData());
        var metaData = connection.getMetaData();
        System.out.println(metaData.getDatabaseProductName());
        System.out.println("Version: " + metaData.getDatabaseMajorVersion() + "." + metaData.getDatabaseMinorVersion());
    }
}