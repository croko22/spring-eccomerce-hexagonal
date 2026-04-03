package com.example.ecommerce.shared.infrastructure;

import org.springframework.beans.factory.InitializingBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@TestConfiguration(proxyBeanMethods = false)
@Profile("test")
public class IntegrationFlywayTestBootstrapConfig {

    private static final Logger log = LoggerFactory.getLogger(IntegrationFlywayTestBootstrapConfig.class);

    @Bean
    InitializingBean integrationSchemaDiagnostics(DataSource dataSource) {
        return () -> {
            String activeSchema = resolveCurrentSchema(dataSource);
            boolean flywayHistoryExists = tableExistsInSchema(dataSource, activeSchema, "flyway_schema_history");
            boolean cartItemsExists = tableExistsInSchema(dataSource, activeSchema, "cart_items");

            log.info(
                    "IT bootstrap diagnostics: activeSchema={}, flywayHistoryExists={}, cartItemsExists={}",
                    activeSchema,
                    flywayHistoryExists,
                    cartItemsExists
            );
        };
    }

    private String resolveCurrentSchema(DataSource dataSource) {
        String sql = "select current_schema()";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            return "unknown";
        } catch (SQLException ex) {
            return "unresolved(" + ex.getClass().getSimpleName() + ")";
        }
    }

    private boolean tableExistsInSchema(DataSource dataSource, String schema, String tableName) {
        String sql = """
                select exists (
                    select 1
                    from information_schema.tables
                    where table_schema = ?
                      and table_name = ?
                )
                """;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, schema);
            statement.setString(2, tableName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
    }
}
