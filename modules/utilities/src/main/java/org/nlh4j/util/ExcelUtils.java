/*
 * @(#)ExcelUtils.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nlh4j.handlers.TemporaryFileHandler;
import org.springframework.util.Assert;

/**
 * Excel utilities<br>
 * <b><u><i>NOTE:</i></u></b> Re-check deprecation code if upgrading Apache POI to 4.0 or later
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version 1.0 Aug 18, 2016
 */
@SuppressWarnings("deprecation")
public final class ExcelUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Open the excel file path or resource path as {@link Workbook}.<br>
     * See more at {@link WorkbookFactory}
     *
     * @param filePathOrResource the path to file or resource (support for wirecast classpath*)
     *
     * @return {@link Workbook}
     *
     * @throws Exception thrown if failed
     */
    public static Workbook openWorkbook(String filePathOrResource) throws Exception {
        Assert.hasText(filePathOrResource, "FilePath Or ResourcePath");

        // parse file or resource stream
        InputStream is = StreamUtils.getFileOrResourceStream(filePathOrResource);
        Assert.notNull(is, "Could not found file or resource by path [" + filePathOrResource + "]");

        // FIXME if JAR input stream, it means this maybe an excel resource stream
        // so require it as file input-stream for avoiding exception:
        // java.util.zip.ZipException: invalid stored block lengths.
        // So using temporary to handle it
        TemporaryFileHandler fileHandler = new TemporaryFileHandler(is);
        StreamUtils.closeQuitely(is);

        // open workbook from temporary file
        Workbook workbook = openWorkbook(fileHandler.getFile());

        // close file handler if necessary
        fileHandler.close();

        // return exported workbook
        return workbook;
    }

    /**
     * Open {@link InputStream} as {@link Workbook}.<br>
     * See more at {@link WorkbookFactory}
     *
     * @param is the {@link InputStream} to read data from.
     *
     * @return {@link Workbook}
     *
     * @throws Exception thrown if failed
     */
    public static Workbook openWorkbook(InputStream is) throws Exception {
    	Workbook workbook = null;
    	try {
    		workbook = WorkbookFactory.create(is);
    	} catch (Exception e1) {
    		try {
    			workbook = new XSSFWorkbook(OPCPackage.open(is));
    		} catch (Exception e2) {
    			LogUtils.logError(ExcelUtils.class, e1);
    			LogUtils.logError(ExcelUtils.class, e2);
    			workbook = null;
    		}
    	}
    	return workbook;
    }

    /**
     * Open {@link InputStream} as {@link Workbook}.<br>
     * See more at {@link WorkbookFactory}
     *
     * @param f the {@link File} to read data from.
     *
     * @return {@link Workbook}
     *
     * @throws Exception thrown if failed
     */
    public static Workbook openWorkbook(File f) throws Exception {
    	Workbook workbook = null;
    	try {
    		workbook = WorkbookFactory.create(f);
    	} catch (Exception e1) {
    		try {
    			workbook = new XSSFWorkbook(OPCPackage.openOrCreate(f));
    		} catch (Exception e2) {
    			LogUtils.logError(ExcelUtils.class, e1);
    			LogUtils.logError(ExcelUtils.class, e2);
    			workbook = null;
    		}
    	}
    	return workbook;
    }

    /**
     * Open the excel file path or resource path as {@link Workbook}.<br>
     * See more at {@link WorkbookFactory}
     *
     * @param filePathOrResource the path to file or resource (support for wirecast classpath*)
     * @param password the password that should be used or null if no password is necessary.
     *
     * @return {@link Workbook}
     *
     * @throws Exception thrown if failed
     */
    public static Workbook openWorkbook(String filePathOrResource, String password) throws Exception {
        Assert.hasText(filePathOrResource, "FilePath Or ResourcePath");

        // parse file or resource stream
        InputStream is = StreamUtils.getFileOrResourceStream(filePathOrResource);
        Assert.notNull(is, "Could not found file or resource by path [" + filePathOrResource + "]");

        // FIXME if JAR input stream, it means this maybe an excel resource stream
        // so require it as file input-stream for avoiding exception:
        // java.util.zip.ZipException: invalid stored block lengths.
        // So using temporary to handle it
        TemporaryFileHandler fileHandler = new TemporaryFileHandler(is);
        StreamUtils.closeQuitely(is);

        // open workbook from temporary file
        Workbook workbook = openWorkbook(fileHandler.getFile(), password);

        // close file handler if necessary
        fileHandler.close();

        // return exported workbook
        return workbook;
    }

    /**
     * Open {@link InputStream} as {@link Workbook}.<br>
     * See more at {@link WorkbookFactory}
     *
     * @param f the {@link File} to read data from.
     * @param password the password that should be used or null if no password is necessary.
     *
     * @return {@link Workbook}
     *
     * @throws Exception thrown if failed
     */
    public static Workbook openWorkbook(File f, String password) throws Exception {
    	Workbook workbook = null;
    	try {
    		workbook = WorkbookFactory.create(f, password);
    	} catch (Exception e) {
			LogUtils.logError(ExcelUtils.class, e);
			workbook = null;
    	}
    	return workbook;
    }

    /**
     * Open {@link InputStream} as {@link Workbook}.<br>
     * See more at {@link WorkbookFactory}
     *
     * @param is the {@link InputStream} to read data from.
     * @param password the password that should be used or null if no password is necessary.
     *
     * @return {@link Workbook}
     *
     * @throws Exception thrown if failed
     */
    public static Workbook openWorkbook(InputStream is, String password) throws Exception {
    	Workbook workbook = null;
    	try {
    		workbook = WorkbookFactory.create(is, password);
    	} catch (Exception e) {
			LogUtils.logError(ExcelUtils.class, e);
			workbook = null;
    	}
    	return workbook;
    }

    //    /**
    //     * Open {@link InputStream} as {@link Workbook}.<br>
    //     * See more at {@link WorkbookFactory}<br>
    //     * (Required version >= 3.10)
    //     *
    //     * @param f the {@link File} to read data from.
    //     * @param password the password that should be used or null if no password is necessary.
    //     * @param readOnly if the Workbook should be opened in read-only mode
    //     * to avoid writing back changes when the document is closed.
    //     *
    //     * @return {@link Workbook}
    //     *
    //     * @throws Exception thrown if failed
    //     */
    //    public static Workbook openWorkbook(File f, String password, boolean readOnly) throws Exception {
    //        return WorkbookFactory.create(f, password, readOnly);
    //    }

    /**
     * Open {@link Sheet} of the specified {@link Workbook} with the specified name.
     *
     * @param wb {@link Workbook} to open
     * @param sheetName sheet name
     *
     * @return {@link Sheet}
     */
    public static Sheet findSheet(Workbook wb, String sheetName) {
        return findSheet(wb, sheetName, false);
    }
    /**
     * Open {@link Sheet} of the specified {@link Workbook} with the specified name.
     *
     * @param wb {@link Workbook} to open
     * @param sheetName sheet name
     * @param createIfNotFound specify creating sheet with the specified name if not found
     *
     * @return {@link Sheet}
     */
    public static Sheet findSheet(Workbook wb, String sheetName, boolean createIfNotFound) {
        Sheet sht = (wb == null || !StringUtils.hasText(sheetName)
                ? null : wb.getSheet(sheetName));
        if (sht == null && createIfNotFound) {
            sht = wb.createSheet(sheetName);
        }
        return sht;
    }

    /**
     * Open {@link Sheet} of the specified {@link Workbook} with the sheet index.
     *
     * @param wb {@link Workbook}
     * @param idx sheet index. 0-based index
     *
     * @return {@link Sheet}
     */
    public static Sheet findSheet(Workbook wb, int idx) {
        return findSheet(wb, idx, false);
    }
    /**
     * Open {@link Sheet} of the specified {@link Workbook} with the sheet index.
     *
     * @param wb {@link Workbook}
     * @param idx sheet index. 0-based index
     * @param createIfNotFound specify creating sheet with the default name if not found.
     *
     * @return {@link Sheet}
     */
    public static Sheet findSheet(Workbook wb, int idx, boolean createIfNotFound) {
        Sheet sht = (wb == null || idx < 0 || idx >= wb.getNumberOfSheets()
                ? null : wb.getSheetAt(idx));
        if (sht == null && createIfNotFound) {
            sht = wb.createSheet();
        }
        return sht;
    }

    /**
     * Open {@link InputStream} as {@link Sheet} with the specified name.
     *
     * @param is the {@link InputStream} to read data from.
     * @param sheetName sheet name
     *
     * @return {@link Sheet}
     *
     * @throws Exception thrown if failed
     */
    public static Sheet findSheet(InputStream is, String sheetName) throws Exception {
        return findSheet(openWorkbook(is), sheetName, false);
    }
    /**
     * Open {@link InputStream} as {@link Sheet} with the specified name.
     *
     * @param is the {@link InputStream} to read data from.
     * @param sheetName sheet name
     * @param createIfNotFound specify creating sheet with the specified name if not found.
     *
     * @return {@link Sheet}
     *
     * @throws Exception thrown if failed
     */
    public static Sheet findSheet(InputStream is, String sheetName, boolean createIfNotFound) throws Exception {
        return findSheet(openWorkbook(is), sheetName, createIfNotFound);
    }

    /**
     * Open {@link InputStream} as {@link Sheet} with the sheet index.
     *
     * @param is the {@link InputStream} to read data from.
     * @param idx sheet index. 0-based index
     *
     * @return {@link Sheet}
     *
     * @throws Exception thrown if failed
     */
    public static Sheet findSheet(InputStream is, int idx) throws Exception {
        return findSheet(openWorkbook(is), idx, false);
    }
    /**
     * Open {@link InputStream} as {@link Sheet} with the sheet index.
     *
     * @param is the {@link InputStream} to read data from.
     * @param idx sheet index. 0-based index
     * @param createIfNotFound specify creating sheet with the default name if not found.
     *
     * @return {@link Sheet}
     *
     * @throws Exception thrown if failed
     */
    public static Sheet findSheet(InputStream is, int idx, boolean createIfNotFound) throws Exception {
        return findSheet(openWorkbook(is), idx, createIfNotFound);
    }

    /**
     * Open {@link File} as {@link Sheet} with the specified name.
     *
     * @param f the {@link File} to read data from.
     * @param sheetName sheet name
     *
     * @return {@link Sheet}
     *
     * @throws Exception thrown if failed
     */
    public static Sheet findSheet(File f, String sheetName) throws Exception {
        return findSheet(openWorkbook(f), sheetName, false);
    }
    /**
     * Open {@link File} as {@link Sheet} with the specified name.
     *
     * @param f the {@link File} to read data from.
     * @param sheetName sheet name
     * @param createIfNotFound specify creating sheet with the specified name if not found.
     *
     * @return {@link Sheet}
     *
     * @throws Exception thrown if failed
     */
    public static Sheet findSheet(File f, String sheetName, boolean createIfNotFound) throws Exception {
        return findSheet(openWorkbook(f), sheetName, createIfNotFound);
    }

    /**
     * Open {@link File} as {@link Sheet} with the sheet index.
     *
     * @param f the {@link File} to read data from.
     * @param idx sheet index. 0-based index
     *
     * @return {@link Sheet}
     *
     * @throws Exception thrown if failed
     */
    public static Sheet findSheet(File f, int idx) throws Exception {
        return findSheet(openWorkbook(f), idx, false);
    }
    /**
     * Open {@link File} as {@link Sheet} with the sheet index.
     *
     * @param f the {@link File} to read data from.
     * @param idx sheet index. 0-based index
     * @param createIfNotFound specify creating sheet with the default name if not found.
     *
     * @return {@link Sheet}
     *
     * @throws Exception thrown if failed
     */
    public static Sheet findSheet(File f, int idx, boolean createIfNotFound) throws Exception {
        return findSheet(openWorkbook(f), idx, createIfNotFound);
    }

    /**
     * Open {@link File} as {@link Sheet} with the specified name.
     *
     * @param f the {@link File} to read data from.
     * @param password the password that should be used or null if no password is necessary.
     * @param sheetName sheet name
     *
     * @return {@link Sheet}
     *
     * @throws Exception thrown if failed
     */
    public static Sheet findSheet(File f, String password, String sheetName) throws Exception {
        return findSheet(openWorkbook(f, password), sheetName, false);
    }
    /**
     * Open {@link File} as {@link Sheet} with the specified name.
     *
     * @param f the {@link File} to read data from.
     * @param password the password that should be used or null if no password is necessary.
     * @param sheetName sheet name
     * @param createIfNotFound specify creating sheet with the specified name if not found.
     *
     * @return {@link Sheet}
     *
     * @throws Exception thrown if failed
     */
    public static Sheet findSheet(File f, String password, String sheetName, boolean createIfNotFound) throws Exception {
        return findSheet(openWorkbook(f, password), sheetName, createIfNotFound);
    }

    /**
     * Open {@link File} as {@link Sheet} with the sheet index.
     *
     * @param f the {@link File} to read data from.
     * @param password the password that should be used or null if no password is necessary.
     * @param idx sheet index. 0-based index
     *
     * @return {@link Sheet}
     *
     * @throws Exception thrown if failed
     */
    public static Sheet findSheet(File f, String password, int idx) throws Exception {
        return findSheet(openWorkbook(f, password), idx, false);
    }
    /**
     * Open {@link File} as {@link Sheet} with the sheet index.
     *
     * @param f the {@link File} to read data from.
     * @param password the password that should be used or null if no password is necessary.
     * @param idx sheet index. 0-based index
     * @param createIfNotFound specify creating sheet with the default name if not found.
     *
     * @return {@link Sheet}
     *
     * @throws Exception thrown if failed
     */
    public static Sheet findSheet(File f, String password, int idx, boolean createIfNotFound) throws Exception {
        return findSheet(openWorkbook(f, password), idx, createIfNotFound);
    }

    /**
     * Open {@link InputStream} as {@link Sheet} with the specified name.
     *
     * @param is the {@link InputStream} to read data from.
     * @param password the password that should be used or null if no password is necessary.
     * @param sheetName sheet name
     *
     * @return {@link Sheet}
     *
     * @throws Exception thrown if failed
     */
    public static Sheet findSheet(InputStream is, String password, String sheetName) throws Exception {
        return findSheet(openWorkbook(is, password), sheetName, false);
    }
    /**
     * Open {@link InputStream} as {@link Sheet} with the specified name.
     *
     * @param is the {@link InputStream} to read data from.
     * @param password the password that should be used or null if no password is necessary.
     * @param sheetName sheet name
     * @param createIfNotFound specify creating sheet with the specified name if not found.
     *
     * @return {@link Sheet}
     *
     * @throws Exception thrown if failed
     */
    public static Sheet findSheet(InputStream is, String password, String sheetName, boolean createIfNotFound) throws Exception {
        return findSheet(openWorkbook(is, password), sheetName, createIfNotFound);
    }

    /**
     * Open {@link InputStream} as {@link Sheet} with the sheet index.
     *
     * @param is the {@link InputStream} to read data from.
     * @param password the password that should be used or null if no password is necessary.
     * @param idx sheet index. 0-based index
     *
     * @return {@link Sheet}
     *
     * @throws Exception thrown if failed
     */
    public static Sheet findSheet(InputStream is, String password, int idx) throws Exception {
        return findSheet(openWorkbook(is, password), idx, false);
    }
    /**
     * Open {@link InputStream} as {@link Sheet} with the sheet index.
     *
     * @param is the {@link InputStream} to read data from.
     * @param password the password that should be used or null if no password is necessary.
     * @param idx sheet index. 0-based index
     * @param createIfNotFound specify creating sheet with the default name if not found.
     *
     * @return {@link Sheet}
     *
     * @throws Exception thrown if failed
     */
    public static Sheet findSheet(InputStream is, String password, int idx, boolean createIfNotFound) throws Exception {
        return findSheet(openWorkbook(is, password), idx, createIfNotFound);
    }

    /**
     * Get cell object
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     *
     * @return {@link Cell} or NULL
     */
    public static Cell findCell(Sheet sheet, int row, int col) {
        return findCell(sheet, row, col, Boolean.TRUE);
    }
    /**
     * Get cell object
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param createIfNoFound specify creating cell if not found
     *
     * @return {@link Cell} or NULL
     */
    public static Cell findCell(Sheet sheet, int row, int col, boolean createIfNoFound) {
        Cell xlCell = null;
        if (sheet != null && row >= 0 && col >= 0) {
            // parse cell row
            Row xlRow = sheet.getRow(row);
            if (xlRow == null && createIfNoFound) {
                xlRow = sheet.createRow(row);
            }
            // parse cell
            xlCell = (xlRow == null ? null : xlRow.getCell(col));
            if (xlCell == null && createIfNoFound) {
                xlCell = xlRow.createCell(col);
            }
        }
        return xlCell;
    }

    /**
     * Get cell type
     *
     * @param xlCell {@link Cell}
     *
     * @return cell type value such as:<br>
     * + 0 - Numeric Cell type {@link Cell#CELL_TYPE_NUMERIC}<br>
     * + 1 - String Cell type {@link Cell#CELL_TYPE_STRING}<br>
     * + 2 - Formula Cell type {@link Cell#CELL_TYPE_FORMULA}<br>
     * + 3 - Empty Cell type {@link Cell#CELL_TYPE_BLANK}<br>
     * + 4 - Boolean Cell type {@link Cell#CELL_TYPE_BOOLEAN}<br>
     * + 5 - Error Cell type {@link Cell#CELL_TYPE_ERROR}<br>
     * + -1 - Could not parse cell type
     */
    public static int getCellType(Cell xlCell) {
        try {
            return (xlCell != null ? xlCell.getCellType() : -1);
        } catch (Exception e) {
            LogUtils.logError(ExcelUtils.class, e.getMessage());
            return -1;
        }
    }
    /**
     * Get cell type
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     *
     * @return cell type value such as:<br>
     * + 0 - Numeric Cell type {@link Cell#CELL_TYPE_NUMERIC}<br>
     * + 1 - String Cell type {@link Cell#CELL_TYPE_STRING}<br>
     * + 2 - Formula Cell type {@link Cell#CELL_TYPE_FORMULA}<br>
     * + 3 - Empty Cell type {@link Cell#CELL_TYPE_BLANK}<br>
     * + 4 - Boolean Cell type {@link Cell#CELL_TYPE_BOOLEAN}<br>
     * + 5 - Error Cell type {@link Cell#CELL_TYPE_ERROR}<br>
     * + -1 - Could not parse cell type
     */
    public static int getCellType(Sheet sheet, int row, int col) {
        int cellType = -1;
        if (sheet != null && 0 <= row && 0 <= col) {
            Row xlRow = sheet.getRow(row);
            if (xlRow != null) {
                cellType = getCellType(xlRow.getCell(col));
            }
        }
        return cellType;
    }

    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     *
     * @return cell value
     */
    public static String getCellStringValue(Cell xlCell) {
        String cellVal = null;
        if (xlCell != null) {
            try {
                switch (xlCell.getCellType()) {
                    case Cell.CELL_TYPE_BOOLEAN: {
                        cellVal = (xlCell.getBooleanCellValue() ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
                        break;
                    }
                    case Cell.CELL_TYPE_NUMERIC: {
                        cellVal = NumberToTextConverter.toText(xlCell.getNumericCellValue());
                        break;
                    }
                    case Cell.CELL_TYPE_STRING: {
                        cellVal = xlCell.getStringCellValue();
                        break;
                    }
                    case Cell.CELL_TYPE_FORMULA: {
                        try {
                            Workbook workbook = xlCell.getSheet().getWorkbook();
                            CreationHelper creationHelper = workbook.getCreationHelper();
                            FormulaEvaluator evaluator = creationHelper.createFormulaEvaluator();
                            CellValue cellValue = evaluator.evaluate(xlCell);
                            cellVal = cellValue.getStringValue();
                        } catch (Exception e) {
                            cellVal = xlCell.getCellFormula();
                        }
                        break;
                    }
                    case Cell.CELL_TYPE_ERROR: {
                        cellVal = String.valueOf(xlCell.getErrorCellValue());
                        break;
                    }
                    default:
                        break;
                }
            } catch (Exception e) {
                LogUtils.logError(ExcelUtils.class, e.getMessage());
                cellVal = null;
            }
        } else LogUtils.logWarn(ExcelUtils.class, "Could not get value of NULL cell object!!!");
        return cellVal;
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     *
     * @return cell value
     */
	public static String getCellStringValue(Sheet sheet, int row, int col) {
        String cellVal = null;
        if (sheet != null && 0 <= row && 0 <= col) {
            Row xlRow = sheet.getRow(row);
            if (xlRow != null) {
                cellVal = getCellStringValue(xlRow.getCell(col));
            }
        }
        return cellVal;
    }

	/**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     *
     * @return cell value
     */
    public static Integer getCellIntValue(Cell xlCell) {
        String cellVal = getCellStringValue(xlCell);
        return (!StringUtils.hasText(cellVal) ? null : NumberUtils.toInt(cellVal));
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     *
     * @return cell value
     */
    public static Integer getCellIntValue(Sheet sheet, int row, int col) {
        String cellVal = getCellStringValue(sheet, row, col);
        return (!StringUtils.hasText(cellVal) ? null : NumberUtils.toInt(cellVal));
    }
    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     * @param defVal default value
     *
     * @return cell value
     */
    public static int getCellIntValue(Cell xlCell, int defVal) {
        String cellVal = getCellStringValue(xlCell);
        return (!StringUtils.hasText(cellVal) ? defVal : NumberUtils.toInt(cellVal, defVal));
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param defVal default value
     *
     * @return cell value
     */
    public static int getCellIntValue(Sheet sheet, int row, int col, int defVal) {
        String cellVal = getCellStringValue(sheet, row, col);
        return (!StringUtils.hasText(cellVal) ? defVal : NumberUtils.toInt(cellVal, defVal));
    }

    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     *
     * @return cell value
     */
    public static Double getCellDoubleValue(Cell xlCell) {
        String cellVal = getCellStringValue(xlCell);
        return (!StringUtils.hasText(cellVal) ? null : NumberUtils.toDouble(cellVal));
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     *
     * @return cell value
     */
    public static Double getCellDoubleValue(Sheet sheet, int row, int col) {
        String cellVal = getCellStringValue(sheet, row, col);
        return (!StringUtils.hasText(cellVal) ? null : NumberUtils.toDouble(cellVal));
    }
    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     * @param defVal default value
     *
     * @return cell value
     */
    public static double getCellDoubleValue(Cell xlCell, double defVal) {
        String cellVal = getCellStringValue(xlCell);
        return (!StringUtils.hasText(cellVal) ? defVal : NumberUtils.toDouble(cellVal, defVal));
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param defVal default value
     *
     * @return cell value
     */
    public static double getCellDoubleValue(Sheet sheet, int row, int col, double defVal) {
        String cellVal = getCellStringValue(sheet, row, col);
        return (!StringUtils.hasText(cellVal) ? defVal : NumberUtils.toDouble(cellVal, defVal));
    }

    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     *
     * @return cell value
     */
    public static BigDecimal getCellBigDecimalValue(Cell xlCell) {
        String cellVal = getCellStringValue(xlCell);
        return (!StringUtils.hasText(cellVal) ? null : NumberUtils.toBigDecimal(cellVal));
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     *
     * @return cell value
     */
    public static BigDecimal getCellBigDecimalValue(Sheet sheet, int row, int col) {
        String cellVal = getCellStringValue(sheet, row, col);
        return (!StringUtils.hasText(cellVal) ? null : NumberUtils.toBigDecimal(cellVal));
    }

    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     *
     * @return cell value
     */
    public static BigInteger getCellBigIntegerValue(Cell xlCell) {
        String cellVal = getCellStringValue(xlCell);
        return (!StringUtils.hasText(cellVal) ? null : NumberUtils.toBigInteger(cellVal));
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     *
     * @return cell value
     */
    public static BigInteger getCellBigIntegerValue(Sheet sheet, int row, int col) {
        String cellVal = getCellStringValue(sheet, row, col);
        return (!StringUtils.hasText(cellVal) ? null : NumberUtils.toBigInteger(cellVal));
    }

    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     *
     * @return cell value
     */
    public static Long getCellLongValue(Cell xlCell) {
        String cellVal = getCellStringValue(xlCell);
        return (!StringUtils.hasText(cellVal) ? null : NumberUtils.toLong(cellVal));
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     *
     * @return cell value
     */
    public static Long getCellLongValue(Sheet sheet, int row, int col) {
        String cellVal = getCellStringValue(sheet, row, col);
        return (!StringUtils.hasText(cellVal) ? null : NumberUtils.toLong(cellVal));
    }
    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     * @param defVal default value
     *
     * @return cell value
     */
    public static long getCellLongValue(Cell xlCell, long defVal) {
        String cellVal = getCellStringValue(xlCell);
        return (!StringUtils.hasText(cellVal) ? defVal : NumberUtils.toLong(cellVal, defVal));
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param defVal default value
     *
     * @return cell value
     */
    public static long getCellLongValue(Sheet sheet, int row, int col, long defVal) {
        String cellVal = getCellStringValue(sheet, row, col);
        return (!StringUtils.hasText(cellVal) ? defVal : NumberUtils.toLong(cellVal, defVal));
    }

    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     *
     * @return cell value
     */
    public static Float getCellFloatValue(Cell xlCell) {
        String cellVal = getCellStringValue(xlCell);
        return (!StringUtils.hasText(cellVal) ? null : NumberUtils.toFloat(cellVal));
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     *
     * @return cell value
     */
    public static Float getCellFloatValue(Sheet sheet, int row, int col) {
        String cellVal = getCellStringValue(sheet, row, col);
        return (!StringUtils.hasText(cellVal) ? null : NumberUtils.toFloat(cellVal));
    }
    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     * @param defVal default value
     *
     * @return cell value
     */
    public static float getCellFloatValue(Cell xlCell, float defVal) {
        String cellVal = getCellStringValue(xlCell);
        return (!StringUtils.hasText(cellVal) ? defVal : NumberUtils.toFloat(cellVal, defVal));
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param defVal default value
     *
     * @return cell value
     */
    public static float getCellFloatValue(Sheet sheet, int row, int col, float defVal) {
        String cellVal = getCellStringValue(sheet, row, col);
        return (!StringUtils.hasText(cellVal) ? defVal : NumberUtils.toFloat(cellVal, defVal));
    }

    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     *
     * @return cell value
     */
    public static Byte getCellByteValue(Cell xlCell) {
        String cellVal = getCellStringValue(xlCell);
        return (!StringUtils.hasText(cellVal) ? null : NumberUtils.toByte(cellVal));
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     *
     * @return cell value
     */
    public static Byte getCellByteValue(Sheet sheet, int row, int col) {
        String cellVal = getCellStringValue(sheet, row, col);
        return (!StringUtils.hasText(cellVal) ? null : NumberUtils.toByte(cellVal));
    }
    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     * @param defVal default value
     *
     * @return cell value
     */
    public static byte getCellByteValue(Cell xlCell, byte defVal) {
        String cellVal = getCellStringValue(xlCell);
        return (!StringUtils.hasText(cellVal) ? defVal : NumberUtils.toByte(cellVal, defVal));
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param defVal default value
     *
     * @return cell value
     */
    public static byte getCellByteValue(Sheet sheet, int row, int col, byte defVal) {
        String cellVal = getCellStringValue(sheet, row, col);
        return (!StringUtils.hasText(cellVal) ? defVal : NumberUtils.toByte(cellVal, defVal));
    }

    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     * @param tryFormat the date format pattern while excel not format cell as date
     *
     * @return cell value
     */
    public static Date getCellDateValue(Cell xlCell, String tryFormat) {
        Date dt = getCellDateValue(xlCell);
        // try with format pattern
        if (dt == null && StringUtils.hasText(tryFormat)) {
            String cellVal = getCellStringValue(xlCell);
            if (StringUtils.hasText(cellVal)) {
                dt = DateUtils.toTimestamp(cellVal, tryFormat);
            }
        }
        return dt;
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param tryFormat the date format pattern while excel not format cell as date
     *
     * @return cell value
     */
    public static Date getCellDateValue(Sheet sheet, int row, int col, String tryFormat) {
        Date dt = getCellDateValue(sheet, row, col);
        // try with format pattern
        if (dt == null && StringUtils.hasText(tryFormat)) {
            String cellVal = getCellStringValue(sheet, row, col);
            if (StringUtils.hasText(cellVal)) {
                dt = DateUtils.toTimestamp(cellVal, tryFormat);
            }
        }
        return dt;
    }
    /**
     * Get cell value with excel has been formatted as date
     *
     * @param xlCell {@link Cell}
     *
     * @return cell value
     */
    public static Date getCellDateValue(Cell xlCell) {
        Date dt = null;
        if (xlCell != null) {
            try {
                int cellType = getCellType(xlCell);
                switch (cellType) {
                    case Cell.CELL_TYPE_NUMERIC: {
                        if (DateUtil.isCellDateFormatted(xlCell)) {
                            dt = xlCell.getDateCellValue();
                        }
                        break;
                    }
                    default:
                        break;
                }
            } catch (Exception e) {
                LogUtils.logError(ExcelUtils.class, e.getMessage());
                dt = null;
            }
        } else LogUtils.logWarn(ExcelUtils.class, "Could not get value of NULL cell object!!!");
        return dt;
    }
    /**
     * Get cell value with excel has been formatted as date
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     *
     * @return cell value
     */
    public static Date getCellDateValue(Sheet sheet, int row, int col) {
        Date dt = null;
        if (sheet != null && 0 <= row && 0 <= col) {
            Row xlRow = sheet.getRow(row);
            if (xlRow != null) {
                dt = getCellDateValue(xlRow.getCell(col));
            }
        }
        return dt;
    }

    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     * @param tryFormat the date format pattern while excel not format cell as date
     *
     * @return cell value
     */
    public static Timestamp getCellTimestampValue(Cell xlCell, String tryFormat) {
        Timestamp dt = getCellTimestampValue(xlCell);
        // try with format pattern
        if (dt == null && StringUtils.hasText(tryFormat)) {
            String cellVal = getCellStringValue(xlCell);
            if (StringUtils.hasText(cellVal)) {
                dt = DateUtils.toTimestamp(cellVal, tryFormat);
            }
        }
        return dt;
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param tryFormat the date format pattern while excel not format cell as date
     *
     * @return cell value
     */
    public static Timestamp getCellTimestampValue(Sheet sheet, int row, int col, String tryFormat) {
        Timestamp dt = getCellTimestampValue(sheet, row, col);
        // try with format pattern
        if (dt == null && StringUtils.hasText(tryFormat)) {
            String cellVal = getCellStringValue(sheet, row, col);
            if (StringUtils.hasText(cellVal)) {
                dt = DateUtils.toTimestamp(cellVal, tryFormat);
            }
        }
        return dt;
    }
    /**
     * Get cell value with excel has been formatted as date
     *
     * @param xlCell {@link Cell}
     *
     * @return cell value
     */
    public static Timestamp getCellTimestampValue(Cell xlCell) {
        Timestamp dt = null;
        if (xlCell != null) {
            try {
                int cellType = getCellType(xlCell);
                switch (cellType) {
                    case Cell.CELL_TYPE_NUMERIC: {
                        if (DateUtil.isCellDateFormatted(xlCell)) {
                            Date cellDt = xlCell.getDateCellValue();
                            if (cellDt != null) dt = new Timestamp(cellDt.getTime());
                        }
                        break;
                    }
                    default:
                        break;
                }
            } catch (Exception e) {
                LogUtils.logError(ExcelUtils.class, e.getMessage());
                dt = null;
            }
        } else LogUtils.logWarn(ExcelUtils.class, "Could not get value of NULL cell object!!!");
        return dt;
    }
    /**
     * Get cell value with excel has been formatted as date
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     *
     * @return cell value
     */
    public static Timestamp getCellTimestampValue(Sheet sheet, int row, int col) {
        Timestamp dt = null;
        if (sheet != null && 0 <= row && 0 <= col) {
            Row xlRow = sheet.getRow(row);
            if (xlRow != null) {
                dt = getCellTimestampValue(xlRow.getCell(col));
            }
        }
        return dt;
    }

    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     *
     * @return cell value
     */
    public static Boolean getCellBooleanValue(Cell xlCell) {
        String cellVal = getCellStringValue(xlCell);
        if ("YES".equalsIgnoreCase(cellVal)) {
            return Boolean.TRUE;
        } else if ("NO".equalsIgnoreCase(cellVal)) {
            return Boolean.FALSE;
        } if ("1".equalsIgnoreCase(cellVal)) {
            return Boolean.TRUE;
        } else if ("0".equalsIgnoreCase(cellVal)) {
            return Boolean.FALSE;
        } else if (StringUtils.hasText(cellVal)) {
            int numVal = NumberUtils.toInt(cellVal, -1);
            if (numVal != 0 && numVal != 1) {
                return NumberUtils.toBool(cellVal);
            } else {
                return (numVal == 0 ? Boolean.FALSE : Boolean.TRUE);
            }
        } return null;
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     *
     * @return cell value
     */
    public static Boolean getCellBooleanValue(Sheet sheet, int row, int col) {
        String cellVal = getCellStringValue(sheet, row, col);
        if ("YES".equalsIgnoreCase(cellVal)) {
            return Boolean.TRUE;
        } else if ("NO".equalsIgnoreCase(cellVal)) {
            return Boolean.FALSE;
        } if ("1".equalsIgnoreCase(cellVal)) {
            return Boolean.TRUE;
        } else if ("0".equalsIgnoreCase(cellVal)) {
            return Boolean.FALSE;
        } else if (StringUtils.hasText(cellVal)) {
            int numVal = NumberUtils.toInt(cellVal, -1);
            if (numVal != 0 && numVal != 1) {
                return NumberUtils.toBool(cellVal);
            } else {
                return (numVal == 0 ? Boolean.FALSE : Boolean.TRUE);
            }
        } return null;
    }
    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     * @param defVal default value
     *
     * @return cell value
     */
    public static boolean getCellBooleanValue(Cell xlCell, boolean defVal) {
        Boolean cellVal = getCellBooleanValue(xlCell);
        return (cellVal != null ? cellVal.booleanValue() : defVal);
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param defVal default value
     * @return cell value
     */
    public static boolean getCellBooleanValue(Sheet sheet, int row, int col, boolean defVal) {
        Boolean cellVal = getCellBooleanValue(sheet, row, col);
        return (cellVal != null ? cellVal.booleanValue() : defVal);
    }

    /**
     * Get cell value
     *
     * @param xlCell {@link Cell}
     *
     * @return cell value
     */
    public static Object getCellValue(Cell xlCell) {
        Object value = null;
        if (xlCell != null) {
            try {
                switch (xlCell.getCellType()) {
                    case Cell.CELL_TYPE_BOOLEAN:
                        value = xlCell.getBooleanCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        if (DateUtil.isCellDateFormatted(xlCell)) {
                            value = xlCell.getDateCellValue();
                        } else {
                            value = xlCell.getNumericCellValue();
                        }
                        break;
                    case Cell.CELL_TYPE_STRING:
                        value = xlCell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        try {
                            Workbook workbook = xlCell.getSheet().getWorkbook();
                            CreationHelper creationHelper = workbook.getCreationHelper();
                            FormulaEvaluator evaluator = creationHelper.createFormulaEvaluator();
                            CellValue cellValue = evaluator.evaluate(xlCell);
                            value = cellValue.getStringValue();
                        } catch (Exception e) {
                            value = xlCell.getCellFormula();
                        }
                        break;
                    case Cell.CELL_TYPE_ERROR:
                        value = xlCell.getErrorCellValue();
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                LogUtils.logError(ExcelUtils.class, e.getMessage());
                value = null;
            }
        } else LogUtils.logWarn(ExcelUtils.class, "Could not get value of NULL cell object!!!");
        return value;
    }
    /**
     * Get cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     *
     * @return cell value
     */
    public static Object getCellValue(Sheet sheet, int row, int col) {
        Object value = null;
        if (sheet != null && row >= 0 && col >= 0) {
            // parse cell row
            Row xlRow = sheet.getRow(row);
            if (xlRow != null) {
                // parse cell
                value = getCellValue(xlRow.getCell(col));
            }
        }
        return value;
    }

    /**
     * Set cell value
     *
     * @param xlCell {@link Cell}
     * @param value to write
     *
     * @return true for successful; else false
     */
    public static boolean setCellValue(Cell xlCell, Object value) {
        if (xlCell != null) {
            // write value
            if (BeanUtils.isInstanceOf(value, Number.class)) {
                Double dbVal = NumberUtils.toDouble(value);
                if (dbVal != null) {
                    xlCell.setCellValue(dbVal);
                    xlCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                    xlCell.getCellStyle().setAlignment(CellStyle.ALIGN_RIGHT);
                }

            } else if (BeanUtils.isInstanceOf(value, Date.class)) {
                Date dtVal = BeanUtils.safeType(value, Date.class);
                if (dtVal != null) {
                    xlCell.setCellValue(dtVal);
                    xlCell.getCellStyle().setAlignment(CellStyle.ALIGN_RIGHT);
                }

            } else if (BeanUtils.isInstanceOf(value, Calendar.class)) {
                Calendar calVal = BeanUtils.safeType(value, Calendar.class);
                if (calVal != null) {
                    xlCell.setCellValue(calVal);
                    xlCell.getCellStyle().setAlignment(CellStyle.ALIGN_RIGHT);
                }

            } else if (BeanUtils.isInstanceOf(value, RichTextString.class)) {
                RichTextString richVal = BeanUtils.safeType(value, RichTextString.class);
                if (richVal != null) {
                    xlCell.setCellValue(richVal);
                    xlCell.getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
                }

            } else if (value != null) {
                xlCell.setCellValue(value.toString());
                xlCell.getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
            }
            // vertical allignment
            xlCell.getCellStyle().setVerticalAlignment(CellStyle.ALIGN_CENTER);
            xlCell.getSheet().autoSizeColumn(xlCell.getColumnIndex());
            return true;

        } else LogUtils.logWarn(ExcelUtils.class, "Could not set value of NULL cell object!!!");
		return false;
    }
    /**
     * Set cell value
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param value to write
     *
     * @return true for successful; else false
     */
    public static boolean setCellValue(Sheet sheet, int row, int col, Object value) {
        if (sheet != null && row >= 0 && col >= 0) {
            // parse cell row
            Row xlRow = sheet.getRow(row);
            if (xlRow == null) xlRow = sheet.createRow(row);
            // parse cell
            Cell xlCell = xlRow.getCell(col);
            if (xlCell == null) xlCell = xlRow.createCell(col);
            // write value
            return setCellValue(xlCell, value);
        }
        return false;
    }

    /**
     * Set cell formula
     *
     * @param xlCell {@link Cell}
     * @param formula to write
     *
     * @return true for successful; else false
     */
    public static boolean setCellFormula(Cell xlCell, String formula) {
        if (xlCell != null) {
            xlCell.setCellType(Cell.CELL_TYPE_FORMULA);
            xlCell.setCellFormula(formula);
            return true;

        } else LogUtils.logWarn(ExcelUtils.class, "Could not set formula of NULL cell object!!!");
        return false;
    }
    /**
     * Set cell formula
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param formula to write
     *
     * @return true for successful; else false
     */
    public static boolean setCellFormula(Sheet sheet, int row, int col, String formula) {
        if (sheet != null && row >= 0 && col >= 0) {
            // parse cell row
            Row xlRow = sheet.getRow(row);
            if (xlRow == null) xlRow = sheet.createRow(row);
            // parse cell
            Cell xlCell = xlRow.getCell(col);
            if (xlCell == null) xlCell = xlRow.createCell(col);
            // write value
            return setCellFormula(xlCell, formula);
        }
        return false;
    }

    /**
     * Add image at the specified cell
     *
     * @param xlCell location cell
     * @param imagePath image file path
     * @param imageXlsType excel image type such as {@link Workbook#PICTURE_TYPE_PNG}
     *
     * @return true for successful; else false
     */
    public static boolean addImageToCell(Cell xlCell, String imagePath, int imageXlsType) {
    	return (xlCell != null && StringUtils.hasText(imagePath)
    			? addImageToCell(
    					xlCell.getSheet(),
    					xlCell.getRowIndex(),
    					xlCell.getColumnIndex(),
    					imagePath, imageXlsType)
    			: false);
    }
    /**
     * Add image at the specified cell
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param imagePath image file path
     * @param imageXlsType excel image type such as {@link Workbook#PICTURE_TYPE_PNG}
     *
     * @return true for successful; else false
     */
    public static boolean addImageToCell(
    		Sheet sheet, int row, int col,
    		String imagePath, int imageXlsType) {
    	boolean ok = false;
    	if (sheet != null && row >= 0 && col >= 0
    			&& StringUtils.hasText(imagePath)) {
    		// require stream from file path
    		InputStream is = StreamUtils.getFileOrResourceStream(imagePath);
			ok = (is != null && addImageToCell(sheet, row, col, is, imageXlsType));
			StreamUtils.closeQuitely(is);
    	}
    	return ok;
    }

    /**
     * Add image at the specified cell
     *
     * @param xlCell location cell
     * @param imageData image data bytes array
     * @param imageXlsType excel image type such as {@link Workbook#PICTURE_TYPE_PNG}
     *
     * @return true for successful; else false
     */
    public static boolean addImageToCell(Cell xlCell, byte[] imageData, int imageXlsType) {
    	return (xlCell != null && !CollectionUtils.isEmpty(imageData)
    			? addImageToCell(
    					xlCell.getSheet(),
    					xlCell.getRowIndex(),
    					xlCell.getColumnIndex(),
    					imageData, imageXlsType)
    			: false);
    }
    /**
     * Add image at the specified cell
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param imageData image data bytes array
     * @param imageXlsType excel image type such as {@link Workbook#PICTURE_TYPE_PNG}
     *
     * @return true for successful; else false
     */
    public static boolean addImageToCell(
    		Sheet sheet, int row, int col,
    		byte[] imageData, int imageXlsType) {
    	if (sheet != null && row >= 0 && col >= 0
    			&& !CollectionUtils.isEmpty(imageData)) {
    		Workbook workbook = null;
    		CreationHelper helper = null;
    		Drawing drawing = null;
    		ClientAnchor anchor = null;
    		Picture pict = null;
    		try {
				// require workbook
				workbook = sheet.getWorkbook();
				// add image to workbook
				int imgIdx = workbook.addPicture(imageData, imageXlsType);
				// Returns an object that handles instantiating concrete classes
				helper = workbook.getCreationHelper();
				// Creates the top-level drawing patriarch.
				drawing = sheet.createDrawingPatriarch();
				// Create an anchor that is attached to the worksheet
				anchor = helper.createClientAnchor();
				anchor.setCol1(col);
				anchor.setRow1(row);
				// Creates a picture
				pict = drawing.createPicture(anchor, imgIdx);
				// Reset the image to the original size
				pict.resize();
    		} catch (Exception e) {
    			LogUtils.logError(ExcelUtils.class, e);
    		}
    	}
    	return false;
    }

    /**
     * Add image at the specified cell
     *
     * @param xlCell location cell
     * @param is image {@link InputStream}
     * @param imageXlsType excel image type such as {@link Workbook#PICTURE_TYPE_PNG}
     *
     * @return true for successful; else false
     */
    public static boolean addImageToCell(Cell xlCell, InputStream is, int imageXlsType) {
    	return (xlCell != null && is != null
    			? addImageToCell(
    					xlCell.getSheet(),
    					xlCell.getRowIndex(),
    					xlCell.getColumnIndex(),
    					is, imageXlsType)
    			: false);
    }
    /**
     * Add image at the specified cell
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param is image {@link InputStream}
     * @param imageXlsType excel image type such as {@link Workbook#PICTURE_TYPE_PNG}
     *
     * @return true for successful; else false
     */
    public static boolean addImageToCell(
    		Sheet sheet, int row, int col,
    		InputStream is, int imageXlsType) {
    	if (sheet != null && row >= 0 && col >= 0 && is != null) {
    		// stream to bytes array
			byte[] data = StreamUtils.toByteArray(is);
			return (!CollectionUtils.isEmpty(data)
					&& addImageToCell(sheet, row, col, data, imageXlsType));
    	}
    	return false;
    }

    /**
     * Add image at the specified cell
     *
     * @param xlCell location cell
     * @param image {@link BufferedImage}
     * @param imageFormat image format of {@link BufferedImage}
     * @param imageXlsType excel image type such as {@link Workbook#PICTURE_TYPE_PNG}
     *
     * @return true for successful; else false
     */
    public static boolean addImageToCell(Cell xlCell,
    		BufferedImage image, String imageFormat, int imageXlsType) {
    	return (xlCell != null && image != null
    			? addImageToCell(
    					xlCell.getSheet(),
    					xlCell.getRowIndex(),
    					xlCell.getColumnIndex(),
    					image, imageFormat, imageXlsType)
    			: false);
    }
    /**
     * Add image at the specified cell
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param image {@link BufferedImage}
     * @param imageFormat image format of {@link BufferedImage}
     * @param imageXlsType excel image type such as {@link Workbook#PICTURE_TYPE_PNG}
     *
     * @return true for successful; else false
     */
    public static boolean addImageToCell(
    		Sheet sheet, int row, int col,
    		BufferedImage image, String imageFormat, int imageXlsType) {
    	if (sheet != null && row >= 0 && col >= 0 && image != null) {
    		// stream to bytes array
			byte[] data = ImageUtils.toByteArray(image, imageFormat);
			return (!CollectionUtils.isEmpty(data)
					&& addImageToCell(sheet, row, col, data, imageXlsType));
    	}
    	return false;
    }

    /**
     * Set cell type
     *
     * @param xlCell {@link Cell}
     * @param cellType to apply
     *
     * @return true for successful; else false
     */
    public static boolean setCellType(Cell xlCell, int cellType) {
        if (xlCell != null) {
            xlCell.setCellType(Cell.CELL_TYPE_FORMULA);
            return true;

        } else LogUtils.logWarn(ExcelUtils.class, "Could not set type of NULL cell object!!!");
        return false;
    }
    /**
     * Set cell type
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param cellType to apply
     *
     * @return true for successful; else false
     */
    public static boolean setCellType(Sheet sheet, int row, int col, int cellType) {
        if (sheet != null && row >= 0 && col >= 0) {
            // parse cell row
            Row xlRow = sheet.getRow(row);
            if (xlRow == null) xlRow = sheet.createRow(row);
            // parse cell
            Cell xlCell = xlRow.getCell(col);
            if (xlCell == null) xlCell = xlRow.createCell(col);
            // write value
            return setCellType(xlCell, cellType);
        }
        return false;
    }

    /**
     * Check cell whether is empty (not contain any text value)
     *
     * @param xlCell {@link Cell}
     *
     * @return true - empty; else false
     */
    public static boolean isCellEmpty(Cell xlCell) {
        return !StringUtils.hasText(getCellStringValue(xlCell));
    }
    /**
     * Check cell whether is empty (not contain any text value)
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     *
     * @return true - empty; else false
     */
    public static boolean isCellEmpty(Sheet sheet, int row, int col) {
        return !StringUtils.hasText(getCellStringValue(sheet, row, col));
    }

    /**
     * Check row whether is empty
     *
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     *
     * @return true - empty; else false
     */
    public static boolean isRowEmpty(Sheet sheet, int row) {
        boolean empty = true;
        if (sheet != null && 0 < row) {
            Row xlRow = sheet.getRow(row);
            if (xlRow != null) {
                for (int i = 0; i < xlRow.getLastCellNum(); i++) {
                    Cell xlCell = xlRow.getCell(i);
                    if (xlCell != null) {
                        String cellVal = getCellStringValue(xlCell);
                        empty = !StringUtils.hasText(cellVal);
                        if (!empty) break;
                    }
                }
            }
        }
        return empty;
    }

    /**
     * Write the specified data list to the excel {@link Sheet}
     *
     * @param <T> data element class type
     * @param sheet to write
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data to write
     * @param properties data property name to parse property value
     *
     * @return true for successful; false if failed
     */
    public static <T> boolean writeToSheet(
            Sheet sheet, int startRow, int startColumn,
            Class<T> dataClass, T data, String...properties) {
    	List<T> dataLst = new LinkedList<T>();
    	if (data != null) dataLst.add(data);
    	return writeToSheet(sheet, startRow, startColumn, dataClass, dataLst, properties);
    }
    /**
     * Write the specified data list to the excel {@link Sheet}
     *
     * @param <T> data element class type
     * @param sheet to write
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     * @param properties data property name to parse property value
     *
     * @return true for successful; false if failed
     */
    public static <T> boolean writeToSheet(
            Sheet sheet, int startRow, int startColumn,
            Class<T> dataClass, List<T> data, String...properties) {
        Assert.notNull(sheet, "Sheet");
        Assert.notNull(dataClass, "DataClass");

        // parse data map
        List<List<Object>> mapData = BeanUtils.mapPropertyValuesList(dataClass, data, properties);
        if (!CollectionUtils.isEmpty(mapData)) {
            try {
                int row = NumberUtils.max(new int[] { startRow, 0 });
                for(List<Object> values : mapData) {
                    // fill data
                    int col = NumberUtils.max(new int[] { startColumn, 0 });
                    for(Object value : values) {
                        // write value
                        setCellValue(sheet, row, col, value);
                        col++;
                    }
                    row++;
                }
                return true;
            } catch (Exception e) {
            	LogUtils.logError(ExcelUtils.class, e.getMessage());
                throw e;
            }
        } else LogUtils.logWarn(ExcelUtils.class, "Could not parse data to write!");
        return false;
    }
    /**
     * Write the specified data list to the excel {@link Sheet}
     *
     * @param <T> data element class type
     * @param sheet to write
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     * @param properties data property name to apply property value
     *
     * @return the data entities list (by the specified data class) or empty if failed
     */
    public static <T> List<T> readFromSheet(
            Sheet sheet,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass,
            String...properties) {
        Assert.notNull(sheet, "Sheet");
        Assert.notNull(dataClass, "DataClass");

        // detect parameter
        Integer iStartRow = NumberUtils.max(new int[] { startRow, 0 });
        Integer iEndRow = NumberUtils.max(new int[] { endRow, -1 });
        Integer iStartColumn = NumberUtils.max(new int[] { startColumn, 0 });
        Integer iEndColumn = NumberUtils.max(new int[] { endColumn, -1 });
        if (iEndRow.intValue() <= -1) iEndRow = sheet.getLastRowNum();

        // make valid parameters
        iEndRow = (iEndRow.intValue() < 0 ? sheet.getLastRowNum() : iEndRow);
        if (iStartRow.intValue() > iEndRow.intValue()) NumberUtils.swap(iStartRow, iEndRow);
        if (iEndColumn.intValue() >= 0 && iStartColumn.intValue() > iEndColumn.intValue()) {
            NumberUtils.swap(iStartColumn, iEndColumn);
        }

        // read data
        List<T> entities = new LinkedList<T>();
        if (iStartRow.intValue() <= sheet.getLastRowNum()) {
            List<List<Object>> dataLst = new LinkedList<List<Object>>();
            try {
                for(int i = iStartRow.intValue(); i <= iEndRow.intValue(); i++) {
                    List<Object> dataRow = new LinkedList<Object>();
                    // parse cell value
                    for(int j = iStartColumn.intValue(); j <= iEndColumn.intValue(); j++) {
                        dataRow.add(getCellValue(sheet, i, j));
                    }
                    dataLst.add(dataRow);
                }

                // map data matrix to data class
                if (!CollectionUtils.isEmpty(dataLst)) {
                    entities.addAll(BeanUtils.asDataList(dataClass, dataLst, properties));
                }
            } catch (Exception e) {
            	LogUtils.logError(ExcelUtils.class, e.getMessage());
                if (!CollectionUtils.isEmpty(dataLst)) dataLst.clear();
                if (!CollectionUtils.isEmpty(entities)) entities.clear();
                throw e;
            }
        } else LogUtils.logWarn(ExcelUtils.class, "Overflow sheet latest data row!");
        return entities;
    }

    /**
     * Get the index of sheet by the specified name
     *
     * @param workbook to find
     * @param sheetName to detect
     * @param ignoreCase specify comparing case-insensitive
     *
     * @return sheet index (0-based index) or -1 if not found
     */
    public static int indexOf(Workbook workbook, String sheetName, boolean ignoreCase) {
        int idx = -1;
        if (workbook != null && StringUtils.hasText(sheetName)) {
            int sheets = workbook.getNumberOfSheets();
            for(int i = 0; i < sheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                if ((!ignoreCase && sheetName.equals(sheet.getSheetName()))
                        || (ignoreCase && sheetName.equalsIgnoreCase(sheet.getSheetName()))) {
                    idx = i;
                    break;
                }
            }
        }
        return idx;
    }
    /**
     * Get the index of sheet by the specified name case-insensitive
     *
     * @param workbook to find
     * @param sheetName to detect
     *
     * @return sheet index (0-based index) or -1 if not found
     */
    public static int indexOf(Workbook workbook, String sheetName) {
        return indexOf(workbook, sheetName, Boolean.TRUE);
    }
    /**
     * Get the index of sheet by the specified name
     *
     * @param sheet to parse wookbook
     * @param sheetName to detect
     * @param ignoreCase specify comparing case-insensitive
     *
     * @return sheet index (0-based index) or -1 if not found
     */
    public static int indexOf(Sheet sheet, String sheetName, boolean ignoreCase) {
        Workbook workbook = (sheet == null ? null : sheet.getWorkbook());
        return (workbook == null ? -1 : indexOf(workbook, sheetName, ignoreCase));
    }
    /**
     * Get the index of sheet by the specified name case-insensitive
     *
     * @param sheet to parse wookbook
     * @param sheetName to detect
     *
     * @return sheet index (0-based index) or -1 if not found
     */
    public static int indexOf(Sheet sheet, String sheetName) {
        return indexOf(sheet, sheetName, Boolean.TRUE);
    }

    /**
     * Clone the specified {@link Sheet} and apply new sheet name
     *
     * @param sheet to clone
     * @param newSheetName to apply name
     *
     * @return the cloned sheet or NULL if failed
     */
    public static Sheet cloneSheet(Sheet sheet, String newSheetName) {
        return (sheet == null ? null
                : cloneSheet(sheet.getWorkbook(),
                        indexOf(sheet, sheet.getSheetName()), newSheetName));
    }
    /**
     * Clone the specified {@link Sheet} and apply new sheet name
     *
     * @param workbook to find sheet
     * @param sheetIdx to clone
     * @param newSheetName to apply name
     *
     * @return the cloned sheet or NULL if failed
     */
    public static Sheet cloneSheet(Workbook workbook, int sheetIdx, String newSheetName) {
        Sheet clone = null;
        if (workbook != null && 0 <= sheetIdx && sheetIdx < workbook.getNumberOfSheets()) {
            clone = workbook.cloneSheet(sheetIdx);
            sheetIdx = indexOf(workbook, clone.getSheetName());
            workbook.setSheetName(sheetIdx, newSheetName);
        }
        return clone;
    }


    /****************************************************
     * READ DATA FROM EXCEL
     ****************************************************/

    /**
     * Read the data list from the excel {@link InputStream}.<br>
     * {@link InputStream} will be closed automatically after processing
     *
     * @param <T> data element class type
     * @param is to read
     * @param sheetName sheet name need to read data
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     *
     * @return the data entities list by the specified data class or empty/NULL if exception
     *
     * @throws Exception thrown if failed
     */
    public static <T> List<T> readFromExcel(
            InputStream is, String sheetName,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass) throws Exception {
        return readFromExcel(is, sheetName,
                startRow, endRow,
                startColumn, startColumn,
                dataClass, (String[]) null);
    }
    /**
     * Read the data list from the excel {@link InputStream}
     *
     * @param <T> data element class type
     * @param is to read
     * @param sheetName sheet name need to read data
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     * @param properties data property name to apply value
     *
     * @return the data entities list by the specified data class or empty/NULL if exception
     *
     * @throws Exception thrown if failed
     */
    public static <T> List<T> readFromExcel(
            InputStream is, String sheetName,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass, String...properties) throws Exception {
        Assert.notNull(is, "InputStream");
        Assert.notNull(dataClass, "DataClass");
        Workbook workbook = null;
        Sheet sheet = null;

        // data list
        List<T> entities = new LinkedList<T>();
        try {
            // Get workbook from stream
            workbook = openWorkbook(is);
            // Get sheet from the workbook
            sheet = findSheet(workbook, sheetName);
            // read data to sheet
            if (sheet != null) {
                entities.addAll(
                        readFromSheet(
                                sheet, startRow, endRow, startColumn, startColumn,
                                dataClass, properties));
            }
        } catch (Exception e) {
        	LogUtils.logError(ExcelUtils.class, e.getMessage());
            if (!CollectionUtils.isEmpty(entities)) entities.clear();
            throw e;
        } finally {
            StreamUtils.closeQuitely(workbook);
        }
        return entities;
    }

    /**
     * Read the data list from the excel {@link InputStream}.<br>
     * {@link InputStream} will be closed automatically after processing
     *
     * @param <T> data element class type
     * @param is to read
     * @param sheetIdx sheet index. 0-based index
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     *
     * @return the data entities list by the specified data class or empty/NULL if exception
     *
     * @throws Exception thrown if failed
     */
    public static <T> List<T> readFromExcel(
            InputStream is, int sheetIdx,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass) throws Exception {
        return readFromExcel(is, sheetIdx,
                startRow, endRow,
                startColumn, startColumn,
                dataClass, (String[]) null);
    }
    /**
     * Read the data list from the excel {@link InputStream}
     *
     * @param <T> data element class type
     * @param is to read
     * @param sheetIdx sheet index. 0-based index
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     * @param properties data property name to apply value
     *
     * @return the data entities list by the specified data class or empty/NULL if exception
     *
     * @throws Exception thrown if failed
     */
    public static <T> List<T> readFromExcel(
            InputStream is, int sheetIdx,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass, String...properties) throws Exception {
        Assert.notNull(is, "InputStream");
        Assert.notNull(dataClass, "DataClass");
        Workbook workbook = null;
        Sheet sheet = null;

        // data list
        List<T> entities = new LinkedList<T>();
        try {
            // Get workbook from stream
            workbook = openWorkbook(is);
            // Get sheet from the workbook
            sheet = findSheet(workbook, sheetIdx);
            // read data to sheet
            if (sheet != null) {
                entities.addAll(
                        readFromSheet(
                                sheet, startRow, endRow, startColumn, startColumn,
                                dataClass, properties));
            }
        } catch (Exception e) {
        	LogUtils.logError(ExcelUtils.class, e.getMessage());
            if (!CollectionUtils.isEmpty(entities)) entities.clear();
            throw e;
        } finally {
            StreamUtils.closeQuitely(workbook);
        }
        return entities;
    }

    /**
     * Read the data list from the excel {@link File}
     *
     * @param <T> data element class type
     * @param f to read
     * @param sheetName sheet name need to read data
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     *
     * @return the data entities list by the specified data class or empty/NULL if exception
     *
     * @throws Exception thrown if failed
     */
    public static <T> List<T> readFromExcel(
            File f, String sheetName,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass) throws Exception {
        Assert.notNull(f, "File");
        Assert.isTrue(f.exists(), "File not found!");
        return readFromExcel(new FileInputStream(f), sheetName,
                startRow, endRow,
                startColumn, startColumn,
                dataClass, (String[]) null);
    }
    /**
     * Read the data list from the excel {@link File}
     *
     * @param <T> data element class type
     * @param f to read
     * @param sheetName sheet name need to write data
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     * @param properties data property name to apply value
     *
     * @return the data entities list by the specified data class or empty/NULL if exception
     *
     * @throws Exception thrown if failed
     */
    public static <T> List<T> readFromExcel(
            File f, String sheetName,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass, String...properties) throws Exception {
        Assert.notNull(f, "File");
        Assert.isTrue(f.exists(), "File not found!");
        return readFromExcel(new FileInputStream(f), sheetName,
                startRow, endRow,
                startColumn, startColumn,
                dataClass, properties);
    }
    /**
     * Read the data list from the excel {@link File}
     *
     * @param <T> data element class type
     * @param f to read
     * @param sheetIdx sheet index. 0-based index
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     *
     * @return the data entities list by the specified data class or empty/NULL if exception
     *
     * @throws Exception thrown if failed
     */
    public static <T> List<T> readFromExcel(
            File f, int sheetIdx,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass) throws Exception {
        Assert.notNull(f, "File");
        Assert.isTrue(f.exists(), "File not found!");
        return readFromExcel(new FileInputStream(f), sheetIdx,
                startRow, endRow,
                startColumn, startColumn,
                dataClass, (String[]) null);
    }
    /**
     * Read the data list from the excel {@link File}
     *
     * @param <T> data element class type
     * @param f to read
     * @param sheetIdx sheet index. 0-based index
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     * @param properties data property name to apply value
     *
     * @return the data entities list by the specified data class or empty/NULL if exception
     *
     * @throws Exception thrown if failed
     */
    public static <T> List<T> readFromExcel(
            File f, int sheetIdx,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass, String...properties) throws Exception {
        Assert.notNull(f, "File");
        Assert.isTrue(f.exists(), "File not found!");
        return readFromExcel(new FileInputStream(f), sheetIdx,
                startRow, endRow,
                startColumn, startColumn,
                dataClass, properties);
    }

    /**
     * Read the data list from the excel {@link InputStream}
     *
     * @param <T> data element class type
     * @param is to read
     * @param sheetName sheet name need to write data
     * @param password to open excel stream
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     *
     * @return the data entities list by the specified data class or empty/NULL if exception
     *
     * @throws Exception thrown if failed
     */
    public static <T> List<T> readFromExcel(
            InputStream is, String sheetName, String password,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass) throws Exception {
        return readFromExcel(is, sheetName, password,
                startRow, endRow,
                startColumn, startColumn,
                dataClass, (String[]) null);
    }
    /**
     * Read the data list from the excel {@link InputStream}
     *
     * @param <T> data element class type
     * @param is to read
     * @param sheetName sheet name need to write data
     * @param password to open excel stream
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     * @param properties data property name to apply value
     *
     * @return the data entities list by the specified data class or empty/NULL if exception
     *
     * @throws Exception thrown if failed
     */
    public static <T> List<T> readFromExcel(
            InputStream is, String sheetName, String password,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass, String...properties) throws Exception {
        Assert.notNull(is, "InputStream");
        Assert.notNull(dataClass, "DataClass");
        Workbook workbook = null;
        Sheet sheet = null;

        // data list
        List<T> entities = new LinkedList<T>();
        try {
            // Get workbook from stream
            workbook = openWorkbook(is, password);
            // Get sheet from the workbook
            sheet = findSheet(workbook, sheetName);
            // read data to sheet
            if (sheet != null) {
                entities.addAll(
                        readFromSheet(
                                sheet, startRow, endRow, startColumn, startColumn,
                                dataClass, properties));
            }
        } catch (Exception e) {
        	LogUtils.logError(ExcelUtils.class, e.getMessage());
            if (!CollectionUtils.isEmpty(entities)) entities.clear();
            throw e;
        } finally {
            StreamUtils.closeQuitely(workbook);
        }
        return entities;
    }

    /**
     * Read the data list from the excel {@link InputStream}
     *
     * @param <T> data element class type
     * @param is to read
     * @param sheetIdx sheet index. 0-based index
     * @param password to open excel stream
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     *
     * @return the data entities list by the specified data class or empty/NULL if exception
     *
     * @throws Exception thrown if failed
     */
    public static <T> List<T> readFromExcel(
            InputStream is, int sheetIdx, String password,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass) throws Exception {
        return readFromExcel(is, sheetIdx, password,
                startRow, endRow,
                startColumn, startColumn,
                dataClass, (String[]) null);
    }
    /**
     * Read the data list from the excel {@link InputStream}
     *
     * @param <T> data element class type
     * @param is to read
     * @param sheetIdx sheet index. 0-based index
     * @param password to open excel stream
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     * @param properties data property name to apply value
     *
     * @return the data entities list by the specified data class or empty/NULL if exception
     *
     * @throws Exception thrown if failed
     */
    public static <T> List<T> readFromExcel(
            InputStream is, int sheetIdx, String password,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass, String...properties) throws Exception {
        Assert.notNull(is, "InputStream");
        Assert.notNull(dataClass, "DataClass");
        Workbook workbook = null;
        Sheet sheet = null;

        // data list
        List<T> entities = new LinkedList<T>();
        try {
            // Get workbook from stream
            workbook = openWorkbook(is, password);
            // Get sheet from the workbook
            sheet = findSheet(workbook, sheetIdx);
            // read data to sheet
            if (sheet != null) {
                entities.addAll(
                        readFromSheet(
                                sheet, startRow, endRow, startColumn, startColumn,
                                dataClass, properties));
            }
        } catch (Exception e) {
        	LogUtils.logError(ExcelUtils.class, e.getMessage());
            if (!CollectionUtils.isEmpty(entities)) entities.clear();
            throw e;
        } finally {
            StreamUtils.closeQuitely(workbook);
        }
        return entities;
    }

    /**
     * Read the data list from the excel {@link File}
     *
     * @param <T> data element class type
     * @param f to read
     * @param sheetName sheet name need to write data
     * @param password to open excel stream
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     *
     * @return the data entities list by the specified data class or empty/NULL if exception
     *
     * @throws Exception thrown if failed
     */
    public static <T> List<T> readFromExcel(
            File f, String sheetName, String password,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass) throws Exception {
        return readFromExcel(new FileInputStream(f),
                sheetName, password,
                startRow, endRow,
                startColumn, startColumn,
                dataClass, (String[]) null);
    }
    /**
     * Read the data list from the excel {@link File}
     *
     * @param <T> data element class type
     * @param f to read
     * @param sheetName sheet name need to write data
     * @param password to open excel stream
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     * @param properties data property name to apply value
     *
     * @return the data entities list by the specified data class or empty/NULL if exception
     *
     * @throws Exception thrown if failed
     */
    public static <T> List<T> readFromExcel(
            File f, String sheetName, String password,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass, String...properties) throws Exception {
        return readFromExcel(new FileInputStream(f),
                sheetName, password,
                startRow, endRow,
                startColumn, startColumn,
                dataClass, properties);
    }

    /**
     * Read the data list from the excel {@link File}
     *
     * @param <T> data element class type
     * @param f to read
     * @param sheetIdx sheet index. 0-based index
     * @param password to open excel stream
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     *
     * @return the data entities list by the specified data class or empty/NULL if exception
     *
     * @throws Exception thrown if failed
     */
    public static <T> List<T> readFromExcel(
            File f, int sheetIdx, String password,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass) throws Exception {
        return readFromExcel(new FileInputStream(f),
                sheetIdx, password,
                startRow, endRow,
                startColumn, startColumn,
                dataClass, (String[]) null);
    }
    /**
     * Read the data list from the excel {@link File}
     *
     * @param <T> data element class type
     * @param f to read
     * @param sheetIdx sheet index. 0-based index
     * @param password to open excel stream
     * @param startRow the start row to read. 0-based row index
     * @param endRow the end row to read. 0-based row index. -1 for reading to end
     * @param startColumn the start column to read. 0-based row index
     * @param endColumn the end column to read. 0-based row index. -1 for reading to end
     * @param dataClass data class
     * @param properties data property name to apply value
     *
     * @return the data entities list by the specified data class or empty/NULL if exception
     *
     * @throws Exception thrown if failed
     */
    public static <T> List<T> readFromExcel(
            File f, int sheetIdx, String password,
            int startRow, int endRow,
            int startColumn, int endColumn,
            Class<T> dataClass, String...properties) throws Exception {
        return readFromExcel(new FileInputStream(f),
                sheetIdx, password,
                startRow, endRow,
                startColumn, startColumn,
                dataClass, properties);
    }

    /****************************************************
     * WRITE DATA TO EXCEL
     ****************************************************/

    /**
     * Write the specified data list to the excel file path or resource path.
     *
     * @param <T> data element class type
     * @param filePathOrResource the path to file or resource (support for wirecast classpath*)
     * @param sheetName sheet name need to write data
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            String filePathOrResource, String sheetName,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data) throws Exception {
        return writeToExcel(filePathOrResource, sheetName, startRow, startColumn,
                dataClass, data, (String[]) null);
    }
    /**
     * Write the specified data list to the excel file path or resource path.
     *
     * @param <T> data element class type
     * @param filePathOrResource the path to file or resource (support for wirecast classpath*)
     * @param sheetName sheet name need to write data
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     * @param properties data property name to parse property value
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            String filePathOrResource, String sheetName,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data,
            String...properties) throws Exception {
        Assert.hasText(filePathOrResource, "FilePath Or ResourcePath");
        Assert.notNull(dataClass, "DataClass");

        // parse file or resource stream
        InputStream is = StreamUtils.getFileOrResourceStream(filePathOrResource);
        Assert.notNull(is, "Could not found file or resource by path [" + filePathOrResource + "]");

        // FIXME if JAR input stream, it means this maybe an excel resource stream
        // so require it as file input-stream for avoiding exception:
        // java.util.zip.ZipException: invalid stored block lengths.
        // So using temporary to handle it
        TemporaryFileHandler fileHandler = new TemporaryFileHandler(is);
        StreamUtils.closeQuitely(is);

        // write to excel as stream
        Workbook workbook = writeToExcel(
                fileHandler.getFile(), sheetName,
                startRow, startColumn, dataClass, data, properties);

        // close file handler if necessary
        fileHandler.close();

        // return exported workbook
        return workbook;
    }

    /**
     * Write the specified data list to the excel file path or resource path.
     *
     * @param <T> data element class type
     * @param filePathOrResource the path to file or resource (support for wirecast classpath*)
     * @param sheetIdx sheet index. 0-based index
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            String filePathOrResource, int sheetIdx,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data) throws Exception {
        return writeToExcel(filePathOrResource, sheetIdx,
                startRow, startColumn,
                dataClass, data, (String[]) null);
    }
    /**
     * Write the specified data list to the excel file path or resource path.
     *
     * @param <T> data element class type
     * @param filePathOrResource the path to file or resource (support for wirecast classpath*)
     * @param sheetIdx sheet index. 0-based index
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     * @param properties data property name to parse property value
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            String filePathOrResource, int sheetIdx,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data,
            String...properties) throws Exception {
        Assert.hasText(filePathOrResource, "FilePath Or ResourcePath");
        Assert.notNull(dataClass, "DataClass");

        // check by physical file path
        InputStream is = StreamUtils.getFileOrResourceStream(filePathOrResource);
        Assert.notNull(is, "Could not found file or resource by path [" + filePathOrResource + "]");

        // FIXME if JAR input stream, it means this maybe an excel resource stream
        // so require it as file input-stream for avoiding exception:
        // java.util.zip.ZipException: invalid stored block lengths.
        // So using temporary to handle it
        TemporaryFileHandler fileHandler = new TemporaryFileHandler(is);
        StreamUtils.closeQuitely(is);

        // write to excel as stream
        Workbook workbook = writeToExcel(
                fileHandler.getFile(), sheetIdx,
                startRow, startColumn, dataClass, data, properties);

        // close file handler if necessary
        fileHandler.close();

        // return exported workbook
        return workbook;
    }

    /**
     * Write the specified data list to the excel file path or resource path.
     *
     * @param <T> data element class type
     * @param filePathOrResource the path to file or resource (support for wirecast classpath*)
     * @param sheetName sheet name need to write data
     * @param password to open excel stream
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            String filePathOrResource, String sheetName, String password,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data) throws Exception {
        return writeToExcel(filePathOrResource, sheetName, password,
                startRow, startColumn,
                dataClass, data, (String[]) null);
    }
    /**
     * Write the specified data list to the excel file path or resource path.
     *
     * @param <T> data element class type
     * @param filePathOrResource the path to file or resource (support for wirecast classpath*)
     * @param sheetName sheet name need to write data
     * @param password to open excel stream
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     * @param properties data property name to parse property value
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            String filePathOrResource, String sheetName, String password,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data,
            String...properties) throws Exception {
        Assert.hasText(filePathOrResource, "FilePath Or ResourcePath");
        Assert.notNull(dataClass, "DataClass");

        // parse file or resource stream
        InputStream is = StreamUtils.getFileOrResourceStream(filePathOrResource);
        Assert.notNull(is, "Could not found file or resource by path [" + filePathOrResource + "]");

        // FIXME if JAR input stream, it means this maybe an excel resource stream
        // so require it as file input-stream for avoiding exception:
        // java.util.zip.ZipException: invalid stored block lengths.
        // So using temporary to handle it
        TemporaryFileHandler fileHandler = new TemporaryFileHandler(is);
        StreamUtils.closeQuitely(is);

        // write to excel as stream
        Workbook workbook = writeToExcel(
                fileHandler.getFile(), sheetName, password,
                startRow, startColumn, dataClass, data, properties);

        // close file handler if necessary
        fileHandler.close();

        // return exported workbook
        return workbook;
    }

    /**
     * Write the specified data list to the excel file path or resource path.
     *
     * @param <T> data element class type
     * @param filePathOrResource the path to file or resource (support for wirecast classpath*)
     * @param sheetIdx sheet index. 0-based index
     * @param password to open excel stream
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            String filePathOrResource, int sheetIdx, String password,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data) throws Exception {
        return writeToExcel(filePathOrResource, sheetIdx, password,
                startRow, startColumn,
                dataClass, data, (String[]) null);
    }
    /**
     * Write the specified data list to the excel file path or resource path.
     *
     * @param <T> data element class type
     * @param filePathOrResource the path to file or resource (support for wirecast classpath*)
     * @param sheetIdx sheet index. 0-based index
     * @param password to open excel stream
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     * @param properties data property name to parse property value
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            String filePathOrResource, int sheetIdx, String password,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data,
            String...properties) throws Exception {
        Assert.hasText(filePathOrResource, "FilePath Or ResourcePath");
        Assert.notNull(dataClass, "DataClass");

        // check by physical file path
        InputStream is = StreamUtils.getFileOrResourceStream(filePathOrResource);
        Assert.notNull(is, "Could not found file or resource by path [" + filePathOrResource + "]");

        // FIXME if JAR input stream, it means this maybe an excel resource stream
        // so require it as file input-stream for avoiding exception:
        // java.util.zip.ZipException: invalid stored block lengths.
        // So using temporary to handle it
        TemporaryFileHandler fileHandler = new TemporaryFileHandler(is);
        StreamUtils.closeQuitely(is);

        // write to excel as stream
        Workbook workbook = writeToExcel(
                fileHandler.getFile(), sheetIdx, password,
                startRow, startColumn, dataClass, data, properties);

        // close file handler if necessary
        fileHandler.close();

        // return exported workbook
        return workbook;
    }

    /**
     * Write the specified data list to the excel {@link InputStream}.<br>
     * {@link InputStream} will be closed automatically after processing
     *
     * @param <T> data element class type
     * @param is to write
     * @param sheetName sheet name need to write data
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            InputStream is, String sheetName,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data) throws Exception {
        return writeToExcel(is, sheetName, startRow, startColumn,
                dataClass, data, (String[]) null);
    }
    /**
     * Write the specified data list to the excel {@link InputStream}
     *
     * @param <T> data element class type
     * @param is to write
     * @param sheetName sheet name need to write data
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     * @param properties data property name to parse property value
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            InputStream is, String sheetName,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data,
            String...properties) throws Exception {
        Assert.notNull(is, "InputStream");
        Assert.notNull(dataClass, "DataClass");
        Workbook workbook = null;
        Sheet sheet = null;
        boolean closed = false;

        // Get workbook from stream
        workbook = openWorkbook(is);
        // Get sheet from the workbook
        sheet = findSheet(workbook, sheetName);

        // parse data map
        List<List<Object>> mapData = BeanUtils.mapPropertyValuesList(dataClass, data, properties);
        if (!CollectionUtils.isEmpty(mapData) && sheet != null) {
            try {
                // write data to sheet
                closed = !writeToSheet(sheet, startRow, startColumn, dataClass, data, properties);
            } catch (Exception e) {
            	LogUtils.logError(ExcelUtils.class, e.getMessage());
                closed = true;
                throw e;
            } finally {
                if (closed) {
                    StreamUtils.closeQuitely(workbook);
                    workbook = null;
                }
            }

            // clear data if necessary
        } else if (!CollectionUtils.isEmpty(mapData)) {
            mapData.clear();
        }

        // return exported workbook or empty workbook
        return workbook;
    }

    /**
     * Write the specified data list to the excel {@link InputStream}.<br>
     * {@link InputStream} will be closed automatically after processing
     *
     * @param <T> data element class type
     * @param is to write
     * @param sheetIdx sheet index. 0-based index
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            InputStream is, int sheetIdx,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data) throws Exception {
        return writeToExcel(is, sheetIdx, startRow, startColumn,
                dataClass, data, (String[]) null);
    }
    /**
     * Write the specified data list to the excel {@link InputStream}
     *
     * @param <T> data element class type
     * @param is to write
     * @param sheetIdx sheet index. 0-based index
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     * @param properties data property name to parse property value
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            InputStream is, int sheetIdx,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data,
            String...properties) throws Exception {
        Assert.notNull(is, "InputStream");
        Assert.notNull(dataClass, "DataClass");
        Workbook workbook = null;
        Sheet sheet = null;
        boolean closed = false;

        // Get workbook from stream
        workbook = openWorkbook(is);
        // Get sheet from the workbook
        sheet = findSheet(workbook, sheetIdx);

        // parse data map
        List<List<Object>> mapData = BeanUtils.mapPropertyValuesList(dataClass, data, properties);
        if (!CollectionUtils.isEmpty(mapData) && sheet != null) {
            try {
                // write data to sheet
                closed = !writeToSheet(sheet, startRow, startColumn, dataClass, data, properties);
            } catch (Exception e) {
            	LogUtils.logError(ExcelUtils.class, e.getMessage());
                closed = true;
                throw e;
            } finally {
                if (closed) {
                    StreamUtils.closeQuitely(workbook);
                    workbook = null;
                }
            }

            // clear data if necessary
        } else if (!CollectionUtils.isEmpty(mapData)) {
            mapData.clear();
        }

        // return exported workbook or empty workbook
        return workbook;
    }

    /**
     * Write the specified data list to the excel {@link File}
     *
     * @param <T> data element class type
     * @param f to write
     * @param sheetName sheet name need to write data
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            File f, String sheetName,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data) throws Exception {
        Assert.notNull(f, "File");
        Assert.isTrue(f.exists(), "File not found!");
        return writeToExcel(new FileInputStream(f), sheetName, startRow, startColumn,
                dataClass, data, (String[]) null);
    }
    /**
     * Write the specified data list to the excel {@link File}
     *
     * @param <T> data element class type
     * @param f to write
     * @param sheetName sheet name need to write data
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     * @param properties data property name to parse property value
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            File f, String sheetName,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data, String...properties) throws Exception {
        Assert.notNull(f, "File");
        Assert.isTrue(f.exists(), "File not found!");
        return writeToExcel(new FileInputStream(f), sheetName, startRow, startColumn,
                dataClass, data, properties);
    }
    /**
     * Write the specified data list to the excel {@link File}
     *
     * @param <T> data element class type
     * @param f to write
     * @param sheetIdx sheet index. 0-based index
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            File f, int sheetIdx,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data) throws Exception {
        Assert.notNull(f, "File");
        Assert.isTrue(f.exists(), "File not found!");
        return writeToExcel(new FileInputStream(f), sheetIdx, startRow, startColumn,
                dataClass, data, (String[]) null);
    }
    /**
     * Write the specified data list to the excel {@link File}
     *
     * @param <T> data element class type
     * @param f to write
     * @param sheetIdx sheet index. 0-based index
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     * @param properties data property name to parse property value
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            File f, int sheetIdx,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data, String...properties) throws Exception {
        Assert.notNull(f, "File");
        Assert.isTrue(f.exists(), "File not found!");
        return writeToExcel(new FileInputStream(f), sheetIdx, startRow, startColumn,
                dataClass, data, properties);
    }

    /**
     * Write the specified data list to the excel {@link InputStream}
     *
     * @param <T> data element class type
     * @param is to write
     * @param sheetName sheet name need to write data
     * @param password to open excel stream
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            InputStream is, String sheetName, String password,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data) throws Exception {
        return writeToExcel(is, sheetName, password, startRow, startColumn,
                dataClass, data, (String[]) null);
    }
    /**
     * Write the specified data list to the excel {@link InputStream}
     *
     * @param <T> data element class type
     * @param is to write
     * @param sheetName sheet name need to write data
     * @param password to open excel stream
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     * @param properties data property name to parse property value
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            InputStream is, String sheetName, String password,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data,
            String...properties) throws Exception {
        Assert.notNull(is, "InputStream");
        Assert.notNull(dataClass, "DataClass");
        Workbook workbook = null;
        Sheet sheet = null;
        boolean closed = false;

        // Get workbook from stream
        workbook = openWorkbook(is, password);
        // Get second sheet from the workbook
        sheet = findSheet(workbook, sheetName);

        // parse data map
        List<List<Object>> mapData = BeanUtils.mapPropertyValuesList(dataClass, data, properties);
        if (!CollectionUtils.isEmpty(mapData) && sheet != null) {
            try {
                // write data to sheet
                closed = !writeToSheet(sheet, startRow, startColumn, dataClass, data, properties);
            } catch (Exception e) {
            	LogUtils.logError(ExcelUtils.class, e.getMessage());
                closed = true;
                throw e;
            } finally {
                if (closed) {
                    StreamUtils.closeQuitely(workbook);
                    workbook = null;
                }
            }

            // clear data if necessary
        } else if (!CollectionUtils.isEmpty(mapData)) {
            mapData.clear();
        }

        // return exported workbook or empty workbook
        return workbook;
    }

    /**
     * Write the specified data list to the excel {@link InputStream}
     *
     * @param <T> data element class type
     * @param is to write
     * @param sheetIdx sheet index. 0-based index
     * @param password to open excel stream
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            InputStream is, int sheetIdx, String password,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data) throws Exception {
        return writeToExcel(is, sheetIdx, password, startRow, startColumn,
                dataClass, data, (String[]) null);
    }
    /**
     * Write the specified data list to the excel {@link InputStream}
     *
     * @param <T> data element class type
     * @param is to write
     * @param sheetIdx sheet index. 0-based index
     * @param password to open excel stream
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     * @param properties data property name to parse property value
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            InputStream is, int sheetIdx, String password,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data,
            String...properties) throws Exception {
        Assert.notNull(is, "InputStream");
        Assert.notNull(dataClass, "DataClass");
        Workbook workbook = null;
        Sheet sheet = null;
        boolean closed = false;

        // Get workbook from stream
        workbook = openWorkbook(is, password);
        // Get second sheet from the workbook
        sheet = findSheet(workbook, sheetIdx);

        // parse data map
        List<List<Object>> mapData = BeanUtils.mapPropertyValuesList(dataClass, data, properties);
        if (!CollectionUtils.isEmpty(mapData) && sheet != null) {
            try {
                // write data to sheet
                closed = !writeToSheet(sheet, startRow, startColumn, dataClass, data, properties);
            } catch (Exception e) {
            	LogUtils.logError(ExcelUtils.class, e.getMessage());
                closed = true;
                throw e;
            } finally {
                if (closed) {
                    StreamUtils.closeQuitely(workbook);
                    workbook = null;
                }
            }

            // clear data if necessary
        } else if (!CollectionUtils.isEmpty(mapData)) {
            mapData.clear();
        }

        // return exported workbook or empty workbook
        return workbook;
    }

    /**
     * Write the specified data list to the excel {@link InputStream}
     *
     * @param <T> data element class type
     * @param f to write
     * @param sheetName sheet name need to write data
     * @param password to open excel stream
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            File f, String sheetName, String password,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data) throws Exception {
        return writeToExcel(new FileInputStream(f),
                sheetName, password, startRow, startColumn,
                dataClass, data, (String[]) null);
    }
    /**
     * Write the specified data list to the excel {@link InputStream}
     *
     * @param <T> data element class type
     * @param f to write
     * @param sheetName sheet name need to write data
     * @param password to open excel stream
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     * @param properties data property name to parse property value
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            File f, String sheetName, String password,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data,
            String...properties) throws Exception {
        return writeToExcel(new FileInputStream(f),
                sheetName, password, startRow, startColumn,
                dataClass, data, properties);
    }

    /**
     * Write the specified data list to the excel {@link InputStream}
     *
     * @param <T> data element class type
     * @param f to write
     * @param sheetIdx sheet index. 0-based index
     * @param password to open excel stream
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            File f, int sheetIdx, String password,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data) throws Exception {
        return writeToExcel(new FileInputStream(f),
                sheetIdx, password, startRow, startColumn,
                dataClass, data, (String[]) null);
    }
    /**
     * Write the specified data list to the excel {@link InputStream}
     *
     * @param <T> data element class type
     * @param f to write
     * @param sheetIdx sheet index. 0-based index
     * @param password to open excel stream
     * @param startRow the start row to fill. 0-based row index
     * @param startColumn the start column to fill. 0-based row index
     * @param dataClass data class
     * @param data data list to write
     * @param properties data property name to parse property value
     *
     * @return {@link Workbook} or NULL if exception (need to close {@link Workbook} after calling this method)
     *
     * @throws Exception thrown if failed and {@link Workbook} will be closed automatically in exception
     */
    public static <T> Workbook writeToExcel(
            File f, int sheetIdx, String password,
            int startRow, int startColumn,
            Class<T> dataClass, List<T> data,
            String...properties) throws Exception {
        return writeToExcel(new FileInputStream(f),
                sheetIdx, password, startRow, startColumn,
                dataClass, data, properties);
    }

    /**
     * Clone {@link CellStyle} from the specified cell
     * @param xlCell to clone
     * @return the cloned {@link CellStyle}
     */
    public static CellStyle cloneStyle(Cell xlCell) {
        return (xlCell != null ? cloneStyle(xlCell.getSheet(), xlCell.getCellStyle()) : null);
    }
    /**
     * Clone {@link CellStyle} from the specified cell
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @return the cloned {@link CellStyle}
     */
    public static CellStyle cloneStyle(Sheet sheet, int row, int col) {
        return (sheet != null && row >= 0 && col >= 0
                ? cloneStyle(findCell(sheet, row, col, true)) : null);
    }
    /**
     * Clone {@link CellStyle} from the specified {@link CellStyle}
     * @param workbook to create new {@link CellStyle}
     * @param srcCellStyle to clone
     * @return the cloned {@link CellStyle}
     */
    public static CellStyle cloneStyle(Workbook workbook, CellStyle srcCellStyle) {
        CellStyle cloned = null;
        if (workbook != null) {
            try {
                cloned = workbook.createCellStyle();
                cloned.cloneStyleFrom(srcCellStyle);
            } catch (Exception e) {
                LogUtils.logWarn(ExcelUtils.class, e.getMessage());
                cloned = null;
            }
        }
        return cloned;
    }
    /**
     * Clone {@link CellStyle} from the specified {@link CellStyle}
     * @param sheet to create new {@link CellStyle}
     * @param srcCellStyle to clone
     * @return the cloned {@link CellStyle}
     */
    public static CellStyle cloneStyle(Sheet sheet, CellStyle srcCellStyle) {
        return (sheet != null ? cloneStyle(sheet.getWorkbook(), srcCellStyle) : null);
    }

    /**
     * Clone {@link Row} cells style
     * from the specified source row to destination row
     * of the specified columns range
     * @param sheet {@link Sheet}
     * @param cellStyle {@link CellStyle} to clone
     * @param destRow destination row index. 0-based row index
     * @param col1 the start/end column index. 0-based column index
     * @param col2 the start/end column index. 0-based column index
     * @return true for successful; else false
     */
    public static boolean cloneRowStyle(
            Sheet sheet, CellStyle cellStyle,
            int destRow, int col1, int col2) {
        boolean ok = false;
        if (sheet != null && cellStyle != null
                && destRow >= 0 && col1 >= 0 && col2 >= 0) {
            try {
                int startCol = NumberUtils.min(new Integer[] { col1, col2 });
                int endCol = NumberUtils.max(new Integer[] { col1, col2 });
                for(int i = startCol; i <= endCol; i++) {
                    Cell xlCell = findCell(sheet, destRow, i);
                    CellStyle cloned = cloneStyle(sheet, cellStyle);
                    if (cloned != null) xlCell.setCellStyle(cloned);
                }
                ok = true;
            } catch (Exception e) {
                LogUtils.logWarn(ExcelUtils.class, e.getMessage());
                ok = false;
            }
        }
        return ok;
    }
    /**
     * Clone {@link Row} cells style
     * from the specified source row to destination row
     * of the specified columns range
     * @param sheet {@link Sheet}
     * @param srcRow source row index. 0-based row index
     * @param destRow destination row index. 0-based row index
     * @param col1 the start/end column index. 0-based column index
     * @param col2 the start/end column index. 0-based column index
     * @return true for successful; else false
     */
    public static boolean cloneRowStyle(
            Sheet sheet, int srcRow, int destRow,
            int col1, int col2) {
        boolean ok = false;
        if (sheet != null && srcRow >= 0 && destRow >= 0
                && col1 >= 0 && col2 >= 0) {
            try {
                int startCol = NumberUtils.min(new Integer[] { col1, col2 });
                int endCol = NumberUtils.max(new Integer[] { col1, col2 });
                for(int i = startCol; i <= endCol; i++) {
                    Cell xlCell = findCell(sheet, destRow, i);
                    CellStyle cloned = cloneStyle(sheet, srcRow, i);
                    if (cloned != null) xlCell.setCellStyle(cloned);
                }
                ok = true;
            } catch (Exception e) {
                LogUtils.logWarn(ExcelUtils.class, e.getMessage());
                ok = false;
            }
        }
        return ok;
    }
    /**
     * Clone {@link Row} cells style
     * from the specified source row to destination row
     * of the specified columns range
     * @param srcSheet source style {@link Sheet}
     * @param srcRow source row index. 0-based row index
     * @param destSheet source style {@link Sheet}
     * @param destRow destination row index. 0-based row index
     * @param col1 the start/end column index. 0-based column index
     * @param col2 the start/end column index. 0-based column index
     * @return true for successful; else false
     */
    public static boolean cloneRowStyle(
            Sheet srcSheet, int srcRow,
            Sheet destSheet, int destRow,
            int col1, int col2) {
        boolean ok = false;
        if (srcSheet != null && srcRow >= 0
                && destSheet != null && destRow >= 0
                && col1 >= 0 && col2 >= 0) {
            try {
                int startCol = NumberUtils.min(new Integer[] { col1, col2 });
                int endCol = NumberUtils.max(new Integer[] { col1, col2 });
                for(int i = startCol; i <= endCol; i++) {
                    Cell xlCell = findCell(destSheet, destRow, i);
                    CellStyle cloned = cloneStyle(srcSheet, srcRow, i);
                    if (cloned != null) xlCell.setCellStyle(cloned);
                }
                ok = true;
            } catch (Exception e) {
                LogUtils.logWarn(ExcelUtils.class, e.getMessage());
                ok = false;
            }
        }
        return ok;
    }
    /**
     * Clone {@link Row} cells style
     * from the specified source row to destination row
     * of the specified columns range
     * @param sheet {@link Sheet}
     * @param cellStyle {@link CellStyle} to clone
     * @param destRow1 the start/end destination row index. 0-based row index
     * @param destRow2 the start/end destination row index. 0-based row index
     * @param col1 the start/end column index. 0-based column index
     * @param col2 the start/end column index. 0-based column index
     * @return true for successful; else false
     */
    public static boolean cloneRowStyle(
            Sheet sheet, CellStyle cellStyle,
            int destRow1, int destRow2,
            int col1, int col2) {
        boolean ok = false;
        if (sheet != null && cellStyle != null
                && destRow1 >= 0 && destRow2 >= 0
                && col1 >= 0 && col2 >= 0) {
            int startRow = NumberUtils.min(new Integer[] { destRow1, destRow2 });
            int endRow = NumberUtils.max(new Integer[] { destRow1, destRow2 });
            for(int i = startRow; i <= endRow; i++) {
                ok = cloneRowStyle(sheet, cellStyle, i, col1, col2);
                if (!ok) break;
            }
        }
        return ok;
    }
    /**
     * Clone {@link Row} cells style
     * from the specified source row to destination row
     * of the specified columns range
     * @param sheet {@link Sheet}
     * @param srcRow source row index. 0-based row index
     * @param destRow1 the start/end destination row index. 0-based row index
     * @param destRow2 the start/end destination row index. 0-based row index
     * @param col1 the start/end column index. 0-based column index
     * @param col2 the start/end column index. 0-based column index
     * @return true for successful; else false
     */
    public static boolean cloneRowStyle(
            Sheet sheet, int srcRow,
            int destRow1, int destRow2,
            int col1, int col2) {
        boolean ok = false;
        if (sheet != null && srcRow >= 0
                && destRow1 >= 0 && destRow2 >= 0
                && col1 >= 0 && col2 >= 0) {
            int startRow = NumberUtils.min(new Integer[] { destRow1, destRow2 });
            int endRow = NumberUtils.max(new Integer[] { destRow1, destRow2 });
            for(int i = startRow; i <= endRow; i++) {
                ok = cloneRowStyle(sheet, srcRow, i, col1, col2);
                if (!ok) break;
            }
        }
        return ok;
    }
    /**
     * Clone {@link Row} cells style
     * from the specified source row to destination row
     * of the specified columns range
     * @param srcSheet source style {@link Sheet}
     * @param srcRow source row index. 0-based row index
     * @param destSheet source style {@link Sheet}
     * @param destRow1 the start/end destination row index. 0-based row index
     * @param destRow2 the start/end destination row index. 0-based row index
     * @param col1 the start/end column index. 0-based column index
     * @param col2 the start/end column index. 0-based column index
     * @return true for successful; else false
     */
    public static boolean cloneRowStyle(
            Sheet srcSheet, int srcRow,
            Sheet destSheet, int destRow1, int destRow2,
            int col1, int col2) {
        boolean ok = false;
        if (srcSheet != null && srcRow >= 0
                && destSheet != null && destRow1 >= 0 && destRow2 >= 0
                && col1 >= 0 && col2 >= 0) {
            try {
                int startRow = NumberUtils.min(new Integer[] { destRow1, destRow2 });
                int endRow = NumberUtils.max(new Integer[] { destRow1, destRow2 });
                int startCol = NumberUtils.min(new Integer[] { col1, col2 });
                int endCol = NumberUtils.max(new Integer[] { col1, col2 });
                for(int i = startRow; i <= endRow; i++) {
                    for(int j = startCol; j <= endCol; j++) {
                        Cell xlCell = findCell(destSheet, i, j);
                        CellStyle cloned = cloneStyle(srcSheet, i, j);
                        if (cloned != null) xlCell.setCellStyle(cloned);
                    }
                }
                ok = true;
            } catch (Exception e) {
                LogUtils.logWarn(ExcelUtils.class, e.getMessage());
                ok = false;
            }
        }
        return ok;
    }

    /**
     * Apply {@link Cell} vertical alignment
     * @param xlCell to apply
     * @param align vertical alignment such as {@link CellStyle#VERTICAL_TOP}
     * @return true for successful; else false
     */
    public static boolean verticalAlign(Cell xlCell, short align) {
        boolean ok = false;
        if (xlCell != null) {
            try {
                xlCell.getCellStyle().setVerticalAlignment(align);
                ok = true;
            } catch (Exception e) {
                LogUtils.logWarn(ExcelUtils.class, e.getMessage());
                ok = false;
            }
        }
        return ok;
    }
    /**
     * Apply {@link Cell} vertical alignment
     * @param sheet {@link Sheet}
     * @param row row index. 0-based row index
     * @param col column index. 0-based column index
     * @param align vertical alignment such as {@link CellStyle#VERTICAL_TOP}
     * @return true for successful; else false
     */
    public static boolean verticalAlign(Sheet sheet, int row, int col, short align) {
        return (sheet != null && row >= 0 && col >= 0
                ? verticalAlign(findCell(sheet, row, col), align) : false);
    }

    /**
     * Apply vertical allignment of cells of the specified {@link Row}
     * @param sheet {@link Sheet}
     * @param row source row index. 0-based row index
     * @param col1 the start/end column index. 0-based column index
     * @param col2 the start/end column index. 0-based column index
     * @param align alignment
     * @return true for successful; else false
     */
    public static boolean verticalAlignRow(
            Sheet sheet, int row, int col1, int col2, short align) {
        boolean ok = false;
        if (sheet != null && row >= 0
                && col1 >= 0 && col2 >= 0) {
            int startCol = NumberUtils.min(new Integer[] { col1, col2 });
            int endCol = NumberUtils.max(new Integer[] { col1, col2 });
            for(int i = startCol; i <= endCol; i++) {
                ok = verticalAlign(sheet, row, i, align);
                if (!ok) break;
            }
        }
        return ok;
    }
}
