package com.example.config.confition;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * アプリケーションが通常のアプリケーションサーバ上で動作している場合に真となる
 * {@link org.springframework.context.annotation.Condition} の実装です。
 */
public class RunOnServerCondition extends EmbeddedContainerChecker {

    @Override
    public ConditionOutcome getMatchOutcome(final ConditionContext context, final AnnotatedTypeMetadata metadata) {
        return !isExistsEmbeddedContainer(context)
                ? ConditionOutcome.match("found Embedded Container")
                : ConditionOutcome.noMatch("did not find Embedded Container");
    }
}
