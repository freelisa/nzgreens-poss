/*
* Copyright (c) 2016 jjshome.com. All Rights Reserved.
*/
package com.nzgreens.console.annotations;

import java.lang.annotation.*;

/**
 * 权限注解。
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PACKAGE, ElementType.TYPE, ElementType.METHOD })
public @interface Auth {
	/** 权限代码 */
	String[] value() default {};
}
