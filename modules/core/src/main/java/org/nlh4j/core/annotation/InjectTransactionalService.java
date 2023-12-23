/*
 * @(#)InjectTransactionalService.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.annotation;

import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;
import org.springframework.transaction.annotation.Transactional;

/**
 * Autowire repository annotation
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@AnnotateWith(annotations = {
		@Annotation(target = AnnotationTarget.CLASS, type = InjectService.class),
		@Annotation(target = AnnotationTarget.CLASS, type = Transactional.class) })
public @interface InjectTransactionalService {}
