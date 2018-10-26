/*
 * @(#)SidebarDto.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.dashboard.dto;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.web.core.dto.ModuleDto;

/**
 * Sidebar DTO information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SidebarDto extends ModuleDto {

	/** */
	private static final long serialVersionUID = 1L;
	/** transient variable for serialization exception */
	private transient List<SidebarDto> children;

	public SidebarDto() {
		this.setChildren(new LinkedList<SidebarDto>());
	}

	public SidebarDto(ModuleDto module) {
		BeanUtils.copyProperties(this, module);
		this.setChildren(new LinkedList<SidebarDto>());
	}

	/**
	 * Read this instance from {@link ObjectInputStream}
	 *
	 * @param is to read
	 */
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream is) {
		try {
			// read all normal properties
			is.defaultReadObject();
			Object children = is.readObject();
			if (CollectionUtils.isCollectionOf(children, SidebarDto.class)) {
				this.setChildren((List<SidebarDto>) BeanUtils.safeType(children, List.class));
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}

	/**
	 * Write this instance to {@link ObjectOutputStream}
	 *
	 * @param os to write
	 */
	private void writeObject(ObjectOutputStream os) {
		try {
			// write all normal properties
			os.defaultWriteObject();
			os.writeObject(this.getChildren());
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}

}
