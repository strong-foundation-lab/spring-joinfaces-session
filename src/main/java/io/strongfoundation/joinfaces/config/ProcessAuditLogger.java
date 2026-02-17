package io.strongfoundation.joinfaces.config;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProcessAuditLogger {
    String process() default "SYSTEM_AUDIT";

    boolean validationUser() default false;
}
