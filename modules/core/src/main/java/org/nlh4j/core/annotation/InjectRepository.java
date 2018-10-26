/*
 * @(#)InjectRepository.java 1.0 Jun 1, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.annotation;

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
		@Annotation(target = AnnotationTarget.CONSTRUCTOR, type = Autowired.class) })
public @interface InjectRepository {}
