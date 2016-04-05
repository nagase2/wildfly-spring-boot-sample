package com.example;

import javax.sql.DataSource;

import org.postgresql.jdbc2.optional.PoolingDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import com.example.config.confition.RunOnEmbeddedContainerCondition;
import com.example.config.confition.RunOnServerCondition;

public class DataSourceConfiguration {

    /**
     * 開発プロファイルでのデータソースを構成する構成クラスです。{@code application.yml} の設定情報に従い、Bitronix
     * の {@link PoolingDataSource} を構築します。
     */
    @Configuration
    @Conditional(RunOnEmbeddedContainerCondition.class)
    public static class Development {

        /**
         * アプリケーションのメインのデータソースに対する構成情報を {@code application.yml} から取り出して返します。
         *
         * @return アプリケーションのメインのデータソースに対する構成情報
         */
        @Bean
        @Primary
        @ConfigurationProperties(prefix = "spring.datasource")
        public DataSourceProperties primaryDatasourceProperties() {
            return new DataSourceProperties();
        }

        /**
         * アプリケーションのサブのデータソースに対する構成情報を {@code application.yml} から取り出して返します。
         *
         * @return アプリケーションのサブのデータソースに対する構成情報
         */
        @Bean
        @ConfigurationProperties(prefix = "secondary.datasource")
        public DataSourceProperties secondaryDatasourceProperties() {
            return new DataSourceProperties();
        }

        /**
         * アプリケーションのメインのデータソースを生成します。
         *
         * @return アプリケーションのメインのデータソース
         */
//        @Bean
//        @Primary
//        public DataSource primaryDataSource() {
//            return new PoolingDataSourceFactory().build(primaryDatasourceProperties());
//        }

        /**
         * アプリケーションのサブのデータソースを生成します。
         *
         * @return アプリケーションのサブのデータソース
         */
//        @Bean
//        public DataSource secondaryDataSource() {
//            return new PoolingDataSourceFactory().build(secondaryDatasourceProperties());
//        }
    }

    /**
     * 実稼働プロファイルでのデータソースを構成する構成クラスです。JNDI 経由でアプリケーションサーバ管理のデータソースを取得します。
     */
    @Configuration
    @Conditional(RunOnServerCondition.class)
    public static class Production {

        /**
         * アプリケーションのメインのデータソースをアプリケーションサーバから取得します。
         *
         * @return アプリケーションのメインのデータソース
         */
        @Bean
        @Primary
        public DataSource primaryDataSource() {
            return new JndiDataSourceLookup().getDataSource("jdbc/primary");
        }

        /**
         * アプリケーションのサブのデータソースをアプリケーションサーバから取得します。
         *
         * @return アプリケーションのサブのデータソース
         */
        @Bean
        public DataSource secondaryDataSource() {
            return new JndiDataSourceLookup().getDataSource("jdbc/secondary");
        }
    }
}
