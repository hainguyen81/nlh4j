/*
 * @(#)BaseUploadParamControllerDto.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.util.CollectionUtils;

/**
 * The bound class of uploading data
 *
 * @param <T> the entity attached data type
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseUploadParamControllerDto<T extends AbstractDto> extends AbstractDto {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Upload file information
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class FileInfo extends AbstractDto {

        /**
         * default serial version id
         */
        private static final long serialVersionUID = 1L;
        private String fileName;
        private MultipartFile file;
    }

    /** upload files */
    private List<FileInfo> files;
    /** upload data */
    private T data;
    public BaseUploadParamControllerDto() {}
    public BaseUploadParamControllerDto(final List<MultipartFile> files, final T data) {
        if (!CollectionUtils.isEmpty(files)) {
            List<FileInfo> fiLst = new LinkedList<FileInfo>();
            for(MultipartFile file : files) {
                final FileInfo fi = new FileInfo();
                fi.setFileName(file.getOriginalFilename());
                fi.setFile(file);
                fiLst.add(fi);
            }
            this.setFiles(fiLst);
        }
        this.setData(data);
    }
    public BaseUploadParamControllerDto(final MultipartHttpServletRequest request, final T data) {
        if (request != null && !CollectionUtils.isEmpty(request.getFileMap())) {
            List<FileInfo> files = new LinkedList<FileInfo>();
            final Map<String, MultipartFile> requestMap = request.getFileMap();
            for(final Iterator<String> it = requestMap.keySet().iterator(); it.hasNext();) {
                String paramName = it.next();
                final MultipartFile file = requestMap.get(paramName);
                final FileInfo fi = new FileInfo();
                fi.setFileName(file.getOriginalFilename());
                fi.setFile(file);
                files.add(fi);
            }
            this.setFiles(files);
        }
        this.setData(data);
    }
}
