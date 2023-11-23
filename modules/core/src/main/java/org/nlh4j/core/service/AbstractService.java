/*
 * @(#)AbstractService.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.service;

import java.io.Serializable;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service Layer common abstract class
 * <p>
 * ServiceレイヤのServiceクラスはこの抽象クラスを継承してください。
 * </p>
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public abstract class AbstractService implements Serializable {

	/** serial uid **/
	private static final long serialVersionUID = 1L;

	/** Logger. **/
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * {@link MessageService}
	 */
	@Inject
	protected MessageService messageService;
}
