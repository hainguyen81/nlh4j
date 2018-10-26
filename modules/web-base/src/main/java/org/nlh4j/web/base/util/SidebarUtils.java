/*
 * @(#)SidebarUtils.java 1.0 Jan 11, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.util;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.nlh4j.util.CollectionUtils;
import org.nlh4j.web.base.dashboard.dto.SidebarDto;
import org.nlh4j.web.core.dto.ModuleDto;

/**
 * {@link SidebarDto} utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class SidebarUtils implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;

	/**
	 * Parse sidebar data from the specified modules list
	 *
	 * @param modules the modules list to parse
	 *
	 * @return sidebar data list
	 */
	public static List<SidebarDto> parseSidebarData(List<ModuleDto> modules) {
		List<SidebarDto> data = new LinkedList<SidebarDto>();
		if (!CollectionUtils.isEmpty(modules)) {
			List<Long> exmodules = new LinkedList<Long>();
			for(ModuleDto module : modules) {
				if (module.getPid() != null && module.getPid() > 0L) continue;
				SidebarDto sidebar = new SidebarDto(module);
				data.add(sidebar);
				exmodules.add(module.getId());
				if (module.getChilds() != null && module.getChilds() > 0) {
					parseSidebarDataRecursive(sidebar, modules, exmodules);
				}
			}
		}
		return data;
	}
	/**
	 * Parse sidebar children modules
	 *
	 * @param sidebar the side bar to parse children
	 * @param modules the modules list
	 * @param exmodules module identities to exclude
	 */
	private static void parseSidebarDataRecursive(SidebarDto sidebar, List<ModuleDto> modules, List<Long> exmodules) {
		if (sidebar == null) return;
		exmodules = (exmodules == null ? new LinkedList<Long>() : exmodules);
		for(ModuleDto module : modules) {
			if (!sidebar.getId().equals(module.getPid())
					|| exmodules.contains(module.getId())) continue;
			SidebarDto csidebar = new SidebarDto(module);
			sidebar.getChildren().add(csidebar);
			exmodules.add(module.getId());
			if (module.getChilds() != null && module.getChilds() > 0) {
				parseSidebarDataRecursive(csidebar, modules, exmodules);
			}
		}
	}
}
