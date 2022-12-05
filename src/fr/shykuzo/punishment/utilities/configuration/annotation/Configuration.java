package fr.shykuzo.punishment.utilities.configuration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Configuration {
	String path();
	
	boolean color() default false;
	char colorCharacter() default '&';
}
