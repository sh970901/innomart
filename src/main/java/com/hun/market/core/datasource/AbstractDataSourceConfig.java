package com.hun.market.core.datasource;

import com.hun.market.core.context.MainContext;
import com.hun.market.core.context.RoutingDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
public abstract class AbstractDataSourceConfig {

    private List<String> packages;

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }


    public List<String> getPackages() {
        return packages;
    }

    @Bean
    @ConfigurationProperties(prefix="spring.datasource.hikari")
    public DataSource leaderDataSource() {
        DataSource d=  DataSourceBuilder.create().build();
        return d;
    }

    @Bean
    @ConfigurationProperties(prefix="spring.datasource.hikari.follower")
    public DataSource followerDataSource() {
        DataSource d=  DataSourceBuilder.create().build();
        return d;
    }

    @Bean
    @ConfigurationProperties(prefix="spring.jpa")
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }

    @Bean
    @Primary
    public DataSource routingDataSource(@Qualifier("leaderDataSource") DataSource leader, @Qualifier("followerDataSource") DataSource follower) {

        var dataSource = new ReplicationRoutingDataSource();
        dataSource.setTargetDataSources(Map.of(MainContext.LEADER, leader, MainContext.FOLLOWER, follower));
        dataSource.setDefaultTargetDataSource(leader);
        return dataSource;
    }

    @Bean("leaderJdbcTemplate")
    @Primary
    public JdbcTemplate leaderJdbcTemplate(@Qualifier("leaderDataSource")  DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean("followerJdbcTemplate")
    public JdbcTemplate followerJdbcTemplate(@Qualifier("followerDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

//    @Bean
//    @Lazy
//    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
//        return new JPAQueryFactory(entityManager);
//    }


    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        var jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

    class ReplicationRoutingDataSource extends AbstractRoutingDataSource {
        public ReplicationRoutingDataSource(){

        }
        @Override
        public Connection getConnection() throws SQLException {
            Connection connection =  determineTargetDataSource().getConnection();
            //log.info("getConnection({}) connection={} info={}", determineCurrentLookupKey(), connection, connection.getClientInfo());
            return connection;
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return determineTargetDataSource().getConnection(username, password);
        }

        @Override
        public void setTargetDataSources(Map<Object, Object> targetDataSources) {
            super.setTargetDataSources(targetDataSources);
        }

        @Override
        protected Object determineCurrentLookupKey() {
            return RoutingDataSource.target();
        }
    }

}
