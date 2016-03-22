package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javafx.application.Application;

/**
 * CommandLineRunnerはコマンドラインで値を取得する場合に使う。
 * aaaxxxs
 * @author nagase
 *
 */
@Configuration
@EntityScan(
		  basePackageClasses = { App2.class, Jsr310JpaConverters.class }
		)
@EnableAutoConfiguration
@ComponentScan
public class App2 extends SpringBootServletInitializer {
  public static void main(String[] args) {
    SpringApplication.run(App2.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(applicationClass);
  }

  private static Class<App2> applicationClass = App2.class;
}
