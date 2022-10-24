package dev.rythem.ambition.detection.check.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CheckInfo {

	public String name();
	public String type();
	public int vl() default 20;
	public CheckType category();
	
}
