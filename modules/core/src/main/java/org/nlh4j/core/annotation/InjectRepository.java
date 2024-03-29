/*
 * @(#)InjectRepository.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.annotation;

import javax.inject.Singleton;

import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Autowire repository annotation
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@AnnotateWith(annotations = {
		@Annotation(target = AnnotationTarget.CLASS, type = Repository.class),
		@Annotation(target = AnnotationTarget.CLASS, type = Singleton.class),
		@Annotation(target = AnnotationTarget.CONSTRUCTOR, type = Autowired.class),
		@Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = javax.inject.Named.class, elements = "\"config\"") })
public @interface InjectRepository {}
