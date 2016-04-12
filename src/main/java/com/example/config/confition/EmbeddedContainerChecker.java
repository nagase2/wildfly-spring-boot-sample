package com.example.config.confition;

import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.util.ClassUtils;

/**
 * 現在の実行環境が組み込みサーブレットコンテナであるか、通常のアプリケーションサーバであるかを判断するための {@link SpringBootCondition} のサブクラスです。このクラスは
 * {@link #isExistsEmbeddedContainer(ConditionContext) 判定を行うだけ}であり、 実際に
 * {@link org.springframework.context.annotation.Condition} の実装として利用するためにはサブクラスによる
 * {@link #getMatchOutcome(ConditionContext, org.springframework.core.type.AnnotatedTypeMetadata)} の実装が必要となります。
 * <p>
 * 現時点では、このクラスはクラスパス中に Embedded Tomcat のクラスが存在するかどうかだけで判定を行っています。
 */
abstract class EmbeddedContainerChecker extends SpringBootCondition {

    private static final String TOMCAT_CLASS_NAME = "org.apache.catalina.startup.Tomcat";

    /**
     * クラスパス中に組み込みサーブレットコンテナのクラスが存在するかどうかを判定します。サブクラスではこの判定結果を利用して、現在の環境を知ることが出来ます。
     *
     * @param context 判定を行うためのコンテキスト情報
     * @return 組み込みサーブレットコンテナ環境で動作していると判断された場合(クラスパス中に組み込みサーブレットコンテナのクラスが存在した場合)は {@code true}、そうでなければ {@code false}
     */
    protected boolean isExistsEmbeddedContainer(final ConditionContext context) {
        return ClassUtils.isPresent(TOMCAT_CLASS_NAME, context.getClassLoader());
    }
}
