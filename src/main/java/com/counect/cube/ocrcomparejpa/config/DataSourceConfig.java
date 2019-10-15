package com.counect.cube.ocrcomparejpa.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "primaryDataSource")
    @Qualifier("primaryDataSource")
    @ConfigurationProperties(prefix="spring.primary.datasource")
    public DataSource primaryDataSource() {
        DataSource build = DataSourceBuilder.create().build();
        return build;
    }

    @Bean(name = "secondaryDataSource")
    @Qualifier("secondaryDataSource")
    @Primary
    @ConfigurationProperties(prefix="spring.secondary.datasource")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "dpsprimaryDataSource")
    @Qualifier("dpsprimaryDataSource")
    @ConfigurationProperties(prefix="spring.dpsprimary.datasource")
    public DataSource dpsprimaryDataSource() {
        DataSource build = DataSourceBuilder.create().build();
        return build;
    }

    @Bean(name = "dpssecondaryDataSource")
    @Qualifier("dpssecondaryDataSource")
    @ConfigurationProperties(prefix="spring.dpssecondary.datasource")
    public DataSource dpssecondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
}
