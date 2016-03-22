package com.example;

import javax.sql.DataSource;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
public class AppConfig2 {
  @Autowired
  DataSourceProperties dataSourceProperties;

  DataSource dataSource;

  @Bean
  @Primary
  @ConfigurationProperties("spring.datasource") // ここを追加
  DataSource realDataSource() {
    DataSourceBuilder factory =
        DataSourceBuilder.create(this.dataSourceProperties.getClassLoader())
            .url(this.dataSourceProperties.getUrl())
            .username(this.dataSourceProperties.getUsername())
            .password(this.dataSourceProperties.getPassword());
    this.dataSource = factory.build();
    return new Log4jdbcProxyDataSource(this.dataSource);
  }

  @Bean
  public AuditorAwareImpl auditorProvider() {
    return new AuditorAwareImpl();
  }
  
}
