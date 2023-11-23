/*
 * @(#)AudioEntity.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.entities;

import android.provider.MediaStore.Audio;
import lombok.Getter;
import lombok.Setter;
import org.nlh4j.android.db.AbstractEntity;

/**
 * {@link Audio} entity
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class AudioEntity extends AbstractEntity {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    /** audio identity */
    @Getter
    @Setter
    private String id;
    /** audio display name */
    @Getter
    @Setter
    private String displayName;
    /** audio title */
    @Getter
    @Setter
    private String title;
    /** audio album identity */
    @Getter
    @Setter
    private Long albumId;
    /** audio album name */
    @Getter
    @Setter
    private String album;
    /** audio artist identity */
    @Getter
    @Setter
    private Long artistId;
    /** audio artist name */
    @Getter
    @Setter
    private String artist;
    /** audio duration in milliseconds */
    @Getter
    @Setter
    private Long duration;
    /** audio bookmark position in milliseconds */
    @Getter
    @Setter
    private Long bookmark;
    /** audio composer */
    @Getter
    @Setter
    private String composer;
    /** audio track number */
    @Getter
    @Setter
    private Long track;
    /** audio year */
    @Getter
    @Setter
    private Integer year;
    /** audio file path */
    @Getter
    @Setter
    private String path;
}
