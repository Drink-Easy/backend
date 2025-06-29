package com.drinkeg.drinkeg;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class DatabaseCleaner implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        entityManager.unwrap(Session.class).doWork(this::extractTableNames);
    }

    private void extractTableNames(Connection connection) {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .map(e -> e.getName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase())
                .toList();
    }

    public void execute() {
        entityManager.unwrap(Session.class).doWork(this::cleanTables);
    }

    private void cleanTables(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // 외래키 제약 조건 비활성화
            statement.execute("SET FOREIGN_KEY_CHECKS = 0");

            for (String tableName : tableNames) {
                // 테이블 내용 삭제
                statement.execute(String.format("TRUNCATE TABLE %s", tableName));
                // AUTO_INCREMENT 초기화
                statement.execute(String.format("ALTER TABLE %s AUTO_INCREMENT = 1", tableName));
            }

            // 외래키 제약 조건 다시 활성화
            statement.execute("SET FOREIGN_KEY_CHECKS = 1");
        }
    }
}