package com.example.flow.manager.proxy;

import com.example.flow.manager.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SubscriptionAspect {

    private final SubscriptionService subscriptionService;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Before("@annotation(CheckSubscription)")
    public void verify(JoinPoint joinPoint, CheckSubscription checkSubscription){

        String login = parseSpelString(joinPoint,checkSubscription.userLogin());

        Long fileSizeInBytes = parseSpelLong(joinPoint,checkSubscription.fileSize());

        subscriptionService.validateUserAccess(login,fileSizeInBytes);
    }

    private String parseSpelString(JoinPoint joinPoint, String expressionStr){
        return parser.parseExpression(expressionStr)
                .getValue(getEvaluationContext(joinPoint), String.class);
    }

    private Long parseSpelLong(JoinPoint joinPoint, String expressionStr){
        return parser.parseExpression(expressionStr)
                .getValue(getEvaluationContext(joinPoint), Long.class);
    }

    private StandardEvaluationContext getEvaluationContext(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();

        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return context;
    }
}
