/*
 * @(#)InjectRepositoryWithConfig.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.annotation;

import javax.inject.Inject;
import javax.inject.Named;

import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;
import org.springframework.stereotype.Repository;

/**
 * Autowire repository annotation
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@AnnotateWith(annotations = {
		@Annotation(target = AnnotationTarget.CLASS, type = Repository.class),
		@Annotation(target = AnnotationTarget.CONSTRUCTOR, type = Inject.class),
	    @Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = Named.class, elements = "\"config\"") })
public @interface InjectRepositoryWithDomaConfig {}
