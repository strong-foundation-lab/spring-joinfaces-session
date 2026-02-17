package io.strongfoundation.joinfaces.config;

import org.springframework.stereotype.Component;
import jakarta.faces.view.ViewScoped;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@ViewScoped
@org.springframework.context.annotation.Scope("view")
public @interface StatelessView {
    String value() default "";
}
