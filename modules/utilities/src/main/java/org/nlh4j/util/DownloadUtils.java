/*
 * @(#)DownloadUtils.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Download utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class DownloadUtils implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Download the specified file path
	 *
	 * @param response the HTTP response to write
	 * @param filePath the file path to download
	 *
	 * @return the HTTP status
	 */
	public static ResponseEntity<HttpStatus> downloadEntity(HttpServletResponse response, String filePath) {
		return downloadEntity(response, filePath, null);
	}
	/**
	 * Download the specified file path alias to the downloaded file name
	 *
	 * @param response the HTTP response to write
	 * @param filePath the file path to download
	 * @param downloadFileName the alias file name
	 *
	 * @return the HTTP status
	 */
	public static ResponseEntity<HttpStatus> downloadEntity(HttpServletResponse response, String filePath, String downloadFileName) {
		return new ResponseEntity<HttpStatus>(downloadStatus(response, filePath, downloadFileName));
	}
	/**
	 * Download the specified file path alias to the downloaded file name
	 *
	 * @param response the HTTP response to write
	 * @param filePath the file path to download
	 * @param downloadFileName the alias file name
	 *
	 * @return the HTTP status
	 */
	public static HttpStatus downloadStatus(HttpServletResponse response, String filePath, String downloadFileName) {
		InputStream is = null;
		HttpStatus status = HttpStatus.NOT_FOUND;
		if (StringUtils.hasText(filePath) && response != null) {
			try {
				File downloadFile = new File(filePath);
				if (!downloadFile.exists() || downloadFile.isDirectory()) {
					status = HttpStatus.NOT_FOUND;
				}
				else {
					downloadFileName = (!StringUtils.hasText(downloadFileName)
							? downloadFile.getName() : downloadFileName);
					is = new FileInputStream(downloadFile);
				    status = downloadStatus(response, is, downloadFileName, Boolean.FALSE);
				}
			}
			catch(Exception e) {
				LogUtils.logError(DownloadUtils.class, e);
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
			finally {
			    StreamUtils.closeQuitely(is);
			}
		}
		return status;
	}

	/**
	 * Download the specified workbook alias the downloaded file name<br>
	 * and {@link Workbook} will be closed automatically after downloading
	 *
	 * @param response the HTTP response to write
	 * @param xlsWrkbook the workbook to download
	 *
	 * @return the HTTP status
	 */
	public static ResponseEntity<HttpStatus> downloadEntity(HttpServletResponse response, Workbook xlsWrkbook) {
		return downloadEntity(response, xlsWrkbook, null);
	}
	/**
	 * Download the specified workbook alias the downloaded file name<br>
	 * and {@link Workbook} will be closed automatically after downloading
	 *
	 * @param response the HTTP response to write
	 * @param xlsWrkbook the workbook to download
	 * @param downloadFileName the alias file name
	 *
	 * @return the HTTP status
	 */
	public static ResponseEntity<HttpStatus> downloadEntity(HttpServletResponse response, Workbook xlsWrkbook, String downloadFileName) {
		return downloadEntity(response, xlsWrkbook, downloadFileName, Boolean.TRUE);
	}
	/**
	 * Download the specified workbook alias the downloaded file name
	 *
	 * @param response the HTTP response to write
	 * @param xlsWrkbook the workbook to download
	 * @param downloadFileName the alias file name
	 * @param closeWorkbook specify {@link Workbook} whether has been closed
	 *
	 * @return the HTTP status
	 */
	public static ResponseEntity<HttpStatus> downloadEntity(HttpServletResponse response, Workbook xlsWrkbook, String downloadFileName, boolean closeWorkbook) {
		return new ResponseEntity<HttpStatus>(downloadStatus(response, xlsWrkbook, downloadFileName, closeWorkbook));
	}
	/**
	 * Download the specified workbook alias the downloaded file name<br>
	 * and {@link Workbook} will be closed automatically after downloading
	 *
	 * @param response the HTTP response to write
	 * @param xlsWrkbook the workbook to download
	 * @param downloadFileName the alias file name
	 *
	 * @return the HTTP status
	 */
	public static HttpStatus downloadStatus(HttpServletResponse response, Workbook xlsWrkbook, String downloadFileName) {
		return downloadStatus(response, xlsWrkbook, downloadFileName, Boolean.TRUE);
	}
	/**
	 * Download the specified workbook alias the downloaded file name
	 *
	 * @param response the HTTP response to write
	 * @param xlsWrkbook the workbook to download
	 * @param downloadFileName the alias file name
	 * @param closeWorkbook specify {@link Workbook} whether has been closed
	 *
	 * @return the HTTP status
	 */
	public static HttpStatus downloadStatus(HttpServletResponse response, Workbook xlsWrkbook, String downloadFileName, boolean closeWorkbook) {
		OutputStream os = null;
		HttpStatus status = HttpStatus.NOT_FOUND;
		if (xlsWrkbook != null && response != null) {
			try {
				downloadFileName = (!StringUtils.hasText(downloadFileName)
						? xlsWrkbook.getSheetName(0) : downloadFileName);
				response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
				response.setHeader("Content-Disposition",
				        RequestUtils.getContentDisposition(downloadFileName));
				response.setHeader("Set-Cookie", "fileDownload=true; path=/");

				os = response.getOutputStream();
				xlsWrkbook.write(os);
			    status = HttpStatus.OK;
			}
			catch(Exception e) {
			    LogUtils.logError(DownloadUtils.class, e);
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
			finally {
				if (closeWorkbook) StreamUtils.closeQuitely(xlsWrkbook);
			}
		}
		return status;
	}

	/**
	 * Download the specified {@link InputStream} alias the downloaded file name<br>
	 * and {@link InputStream} will be closed automatically after downloading
	 *
	 * @param response the HTTP response to write
	 * @param is the stream to download
	 *
	 * @return the HTTP status
	 */
	public static ResponseEntity<HttpStatus> downloadEntity(HttpServletResponse response, InputStream is) {
		return downloadEntity(response, is, null);
	}
	/**
	 * Download the specified {@link InputStream} alias the downloaded file name<br>
	 * and {@link InputStream} will be closed automatically after downloading
	 *
	 * @param response the HTTP response to write
	 * @param is the stream to download
	 * @param downloadFileName the alias file name
	 *
	 * @return the HTTP status
	 */
	public static ResponseEntity<HttpStatus> downloadEntity(HttpServletResponse response, InputStream is, String downloadFileName) {
		return downloadEntity(response, is, downloadFileName, Boolean.TRUE);
	}
	/**
	 * Download the specified {@link InputStream} alias the downloaded file name
	 *
	 * @param response the HTTP response to write
	 * @param is the stream to download
	 * @param downloadFileName the alias file name
	 * @param closeStream specify {@link InputStream} whether has been closed
	 *
	 * @return the HTTP status
	 */
	public static ResponseEntity<HttpStatus> downloadEntity(HttpServletResponse response, InputStream is, String downloadFileName, boolean closeStream) {
		return new ResponseEntity<HttpStatus>(downloadStatus(response, is, downloadFileName, closeStream));
	}
	/**
	 * Download the specified {@link InputStream} alias the downloaded file name
	 *
	 * @param response the HTTP response to write
	 * @param is the stream to download
	 * @param downloadFileName the alias file name. NULL for randomizing file name
	 * @param closeStream specify {@link InputStream} whether has been closed
	 *
	 * @return the HTTP status
	 */
	public static HttpStatus downloadStatus(HttpServletResponse response, InputStream is, String downloadFileName, boolean closeStream) {
		OutputStream os = null;
		HttpStatus status = HttpStatus.NOT_FOUND;
		if (is != null && response != null) {
			try {
				downloadFileName = (!StringUtils.hasText(downloadFileName)
						? RandomStringUtils.randomAlphanumeric(8) : downloadFileName);
				response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
				response.setHeader("Content-Disposition",
				        RequestUtils.getContentDisposition(downloadFileName));
				response.setHeader("Set-Cookie", "fileDownload=true; path=/");

			    os = response.getOutputStream();
			    StreamUtils.copyStream(is, os);
			    status = HttpStatus.OK;
			}
			catch(Exception e) {
			    LogUtils.logError(DownloadUtils.class, e);
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
			finally {
			    if (closeStream) StreamUtils.closeQuitely(is);
			}
		}
		return status;
	}
	/**
	 * Download the specified {@link InputStream} alias the downloaded file name<br>
	 * and {@link InputStream} will be closed automatically after downloading
	 *
	 * @param response the HTTP response to write
	 * @param is the stream to download
	 * @param downloadFileName the alias file name. NULL for randomizing file name
	 *
	 * @return the HTTP status
	 */
	public static HttpStatus downloadStatus(HttpServletResponse response, InputStream is, String downloadFileName) {
		return downloadStatus(response, is, downloadFileName, Boolean.TRUE);
	}
}
