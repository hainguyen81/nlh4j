/*
 * @(#)MasterController.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.controller;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.machinezoo.noexception.Exceptions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.nlh4j.core.annotation.ExecutePermission;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.core.dto.BaseEntityParamControllerDto;
import org.nlh4j.core.dto.BaseSearchParamControllerDto;
import org.nlh4j.core.dto.BaseUploadParamControllerDto;
import org.nlh4j.core.pagination.PaginationSearchDto;
import org.nlh4j.core.pagination.PagingDto;
import org.nlh4j.exceptions.ApplicationValidationException;
import org.nlh4j.util.DownloadUtils;
import org.nlh4j.util.ExceptionUtils;
import org.nlh4j.util.JsonUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * Abstract controller for master screens
 *
 * @param <T> the main entity type
 * @param <C> the entity search condition type
 * @param <PK> the entity primary/unique key type
 * @param <S> the bound class of searching conditions
 * @param <U> the bound class of entity primary/unique key
 * @param <M> the attached upload data type
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController
public abstract class AbstractMasterController
    <T extends AbstractDto, C extends AbstractDto, PK extends AbstractDto,
    S extends BaseSearchParamControllerDto<C>, U extends BaseEntityParamControllerDto<PK>,
    M extends AbstractDto>
    extends AbstractController {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	protected static final String FUNCTION_INSERTABLE_PERMISSION = "INSERTABLE";
	protected static final String FUNCTION_UPDATABLE_PERMISSION = "UPDATABLE";
	protected static final String FUNCTION_DELETABLE_PERMISSION = "DELETABLE";
	protected static final String FUNCTION_WRITABLE_PERMISSION = "WRITABLE";

	/**
	 * Attach more attributes into page view.
	 * TODO Children controllers maybe override this method
	 * to attach more attributes to page if necessary
	 *
	 * @param mav the page view
	 * @param mode page mode
	 *
	 * @return the attached page view
	 */
	protected ModelAndView attachModelAndView(ModelAndView mav, MasterPageType mode) {
	    return mav;
	}

	/**
	 * Index page
	 *
	 * @return the index page
	 */
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView index() {
	    ModelAndView mav = new ModelAndView(this.getIndexPage());
	    return this.attachModelAndView(mav, MasterPageType.HOME);
	}
	/**
	 * Get the index page path
	 * @return the index page path
	 */
	protected abstract String getIndexPage();

	/**
	 * Search entities list for showing manager view
	 *
	 * @param searchPage the search conditions
	 * @param result {@link BindingResult}
	 *
	 * @return entities list or NULL
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public PagingDto<T> search(@RequestBody S searchPage, BindingResult result) {
		// check by bean validation
		this.checkBinding(result);

		// search
		PaginationSearchDto<T, C> search = new PaginationSearchDto<T, C>();
		search.setSearchConditions(searchPage.getC());
		// for changing number records per page
		if (searchPage.getLimit() != null) {
			search.getData().getPagination().setLimit(searchPage.getLimit());

			// initialize as unlimit
		} else {
		    search.getData().getPagination().setLimit(-1);
		}
		search.getData().getPagination().setPageNumber(searchPage.getP());
		search = this.searchEntities(search);
		return (search == null ? null : search.getData());
	}
	/**
	 * Search pagination entities list by search conditions.
	 * TODO children classes should be override this method for searching pagination entities list
	 *
	 * @param search the search condition
	 *
	 * @return pagination entities list or NULL
	 */
	protected PaginationSearchDto<T, C> searchEntities(PaginationSearchDto<T, C> search) {
		return search;
	}

	/**
	 * Get the detail page view
	 *
	 * @param entitypk the entity primary key parameter
	 * @param mav {@link ModelAndView}
	 * @param isNew specify this is new created page
	 * @param editable specify the page whether is viewable/editable
	 *
	 * @return the detail page view
	 */
	protected final ModelAndView detail(ModelAndView mav, PK entitypk, boolean isNew, boolean editable) {
		mav.addObject("isNew", isNew);
		mav.addObject("editable", editable);
		mav.addObject("pk", entitypk);
	    // simple JSON primitive type
		String entityPkJson = null;
		try { entityPkJson = JsonUtils.serialize(entitypk); }
		catch(Exception e1) {
		    try { entityPkJson = JsonUtils.serialize(new BaseEntityParamControllerDto<PK>(entitypk)); }
	        catch(Exception e2) { entityPkJson = null; }
		}
		if (StringUtils.hasText(entityPkJson)) { mav.addObject("pkJson", entityPkJson); }
		return mav;
	}
	/**
	 * Get the index page path
	 * @return the index page path
	 */
	protected abstract String getDetailPage();
	/**
	 * Get the detail view page
	 *
	 * @param entitypk the entity primary key parameter
     * @param result {@link BindingResult}
	 *
	 * @return the detail view page
	 */
	@RequestMapping(value = "/view", method = { RequestMethod.GET, RequestMethod.POST})
	public ModelAndView view(@ModelAttribute PK entitypk, BindingResult result) {
		// check by bean validation
		this.checkBinding(result);

		// attach more attributes if necessary
		ModelAndView mav = new ModelAndView(this.getDetailPage());
		return this.attachModelAndView(
		        // create page view
		        this.detail(mav, Objects.requireNonNull(entitypk, "entitypk"), false, false),
		        MasterPageType.VIEW);
	}
	/**
	 * Get the edit view page
	 *
	 * @param entitypk the entity primary key parameter
     * @param result {@link BindingResult}
	 *
	 * @return the edit view page
	 */
	@RequestMapping(value = "/edit", method = { RequestMethod.GET, RequestMethod.POST})
	@ExecutePermission(value = {
	        FUNCTION_INSERTABLE_PERMISSION,
	        FUNCTION_UPDATABLE_PERMISSION,
	        FUNCTION_WRITABLE_PERMISSION
    })
	public ModelAndView edit(@ModelAttribute PK entitypk, BindingResult result) {
		// check by bean validation
		if (result != null && result.hasErrors()) {
			//	return new ResponseEntity<String>(
			//			JsonUtils.serialize(result.getAllErrors()),
			//			HttpStatus.EXPECTATION_FAILED);
			throw new ApplicationValidationException(result);
		}

		// attach more attributes if necessary
		ModelAndView mav = new ModelAndView(this.getDetailPage());
		return this.attachModelAndView(
		        // create page view
                this.detail(mav, Objects.requireNonNull(entitypk, "entitypk"), false, true),
                MasterPageType.UPDATE);
	}
	/**
	 * Get the create view page
	 *
	 * @return the create view page
	 */
	@RequestMapping(value = "/create", method = { RequestMethod.GET, RequestMethod.POST})
	@ExecutePermission(value = {
            FUNCTION_INSERTABLE_PERMISSION,
            FUNCTION_WRITABLE_PERMISSION
    })
	public ModelAndView create() {
	    // attach more attributes if necessary
	    ModelAndView mav = new ModelAndView(this.getDetailPage());
        return this.attachModelAndView(
                // create page view
                this.detail(mav, null, true, true),
                MasterPageType.NEW);
	}
	/**
	 * Search the entity
	 *
	 * @param entitypk the entity primary key parameter
     * @param result {@link BindingResult}
	 *
	 * @return the entity or NULL
	 */
	@RequestMapping(value = "/entity", method = { RequestMethod.GET, RequestMethod.POST })
	public T searchEntity(@RequestBody(required = true) U entitypk, BindingResult result) {
		// check by bean validation
		this.checkBinding(result);

		// search entity
		return this.findEntityBy(Objects.requireNonNull(entitypk, "entitypk").getPk());
	}
	protected abstract T findEntityBy(PK entitypk);

	/**
	 * Check unique entity
	 *
	 * @param entitypk the entity primary key parameter
     * @param result {@link BindingResult}
	 *
	 * @return true if unique; else false
	 */
	@RequestMapping(value = "/checkUnique", method = RequestMethod.POST)
	public ResponseEntity<String> checkUnique(@RequestBody(required = true) U entitypk, BindingResult result) {
		// check by bean validation
		this.checkBinding(result);

		return (this.isUnique(Objects.requireNonNull(entitypk, "entitypk").getPk())
				? new ResponseEntity<String>((String) null, HttpStatus.CONFLICT)
						: new ResponseEntity<String>((String) null, HttpStatus.OK));
	}
	/**
	 * Check the specified entity whether is unique constraint by the entity primary key
	 *
	 * @param entitypk the entity primary key parameter
	 *
	 * @return true if unique constraint; else false
	 */
	protected abstract boolean isUnique(PK entitypk);

	/**
	 * 受検者を新作成する。
	 *
	 * @param entity to create
     * @param result {@link BindingResult}
	 *
	 * @return response entity
	 */
	@RequestMapping(value = "/create/save", method = RequestMethod.POST)
	@ExecutePermission(value = {
            FUNCTION_INSERTABLE_PERMISSION,
            FUNCTION_WRITABLE_PERMISSION
    })
	public ResponseEntity<String> create(@Valid @RequestBody T entity, BindingResult result) {
		return this.saveOrUpdateEntity(entity, true, result);
	}
	/**
	 * 受検者を新作成する。
	 *
	 * @param entity to create
     * @param result {@link BindingResult}
	 *
	 * @return response entity
	 */
	@RequestMapping(
			value = "/create/save/v2",
			method = RequestMethod.POST,
			headers = "Accept=application/json",
			produces = "application/json"
	)
	@ExecutePermission(value = {
            FUNCTION_INSERTABLE_PERMISSION,
            FUNCTION_WRITABLE_PERMISSION
    })
	public ResponseEntity<String> createModel(@Valid @ModelAttribute T entity, BindingResult result) {
		return this.saveOrUpdateEntity(entity, true, result);
	}
	/**
	 * 受検者を変更する。
	 *
	 * @param entity to update
     * @param result {@link BindingResult}
     *
     * @return response entity
	 */
	@RequestMapping(value = "/update/save", method = RequestMethod.POST)
	@ExecutePermission(value = {
            FUNCTION_INSERTABLE_PERMISSION,
            FUNCTION_UPDATABLE_PERMISSION,
            FUNCTION_WRITABLE_PERMISSION
    })
	public ResponseEntity<String> update(@Valid @RequestBody T entity, BindingResult result) {
		return this.saveOrUpdateEntity(entity, false, result);
	}
	/**
	 * 受検者を変更する。
	 *
	 * @param entity to update
     * @param result {@link BindingResult}
     *
     * @return response entity
	 */
	@RequestMapping(
			value = "/update/save/v2",
			method = RequestMethod.POST,
			headers = "Accept=application/json",
			produces = "application/json"
	)
	@ExecutePermission(value = {
            FUNCTION_INSERTABLE_PERMISSION,
            FUNCTION_UPDATABLE_PERMISSION,
            FUNCTION_WRITABLE_PERMISSION
    })
	public ResponseEntity<String> updateModel(@Valid @ModelAttribute T entity, BindingResult result) {
		return this.saveOrUpdateEntity(entity, false, result);
	}
	/**
	 * 受検者を新作成／変更する。
	 *
	 * @param entity the entity to insert/update
	 * @param create specify inserting/updating
	 *
	 * @return process status
	 */
	protected final ResponseEntity<String> saveOrUpdateEntity(T entity, boolean create) {
		return saveOrUpdateEntity(entity, create, null);
	}
	/**
	 * 受検者を新作成／変更する。
	 *
	 * @param entity the entity to insert/update
	 * @param create specify inserting/updating
	 * @param result binding result (included bean validation)
	 *
	 * @return process status
	 */
	protected final ResponseEntity<String> saveOrUpdateEntity(T entity, boolean create, BindingResult result) {
		// check by bean validation
		this.checkBinding(result);

		// response headers
		HttpHeaders headers = new HttpHeaders();
		this.applyHttpHeaders(headers);

		// check conflict
		if (create && this.existEntity(entity)) {
			return new ResponseEntity<String>(
			        super.getHttpStatusReason(HttpStatus.CONFLICT),
			        headers, HttpStatus.CONFLICT);
		}
		// check not found
		else if (!create && !this.existEntity(entity)) {
			return new ResponseEntity<String>(
                    super.getHttpStatusReason(HttpStatus.NOT_FOUND),
                    headers, HttpStatus.NOT_FOUND);
		}
		// check parameters if necessary
		else if (!this.validateEntity(entity, create)) {
			return new ResponseEntity<String>(
                    super.getHttpStatusReason(HttpStatus.EXPECTATION_FAILED),
                    headers, HttpStatus.EXPECTATION_FAILED);
		}
        // check parameters if necessary
        else {
            ResponseEntity<String> resp = this.validateResponseEntity(entity, create);
            // if response status is error
            if (resp != null
                    && (resp.getStatusCodeValue() < HttpStatus.OK.value()
                            || resp.getStatusCodeValue() >= HttpStatus.MULTIPLE_CHOICES.value())) {
                // re-change status
                String body = resp.getBody();
                if (!StringUtils.hasText(body)) {
                    body = super.getHttpStatusReason(HttpStatus.EXPECTATION_FAILED);
                }
                return new ResponseEntity<String>(body, headers, resp.getStatusCode());
            }
        }

		// save/update
		if (this.doSaveOrUpdate(entity, create) < 0) {
			return new ResponseEntity<String>(
                    super.getInternalServerErrorReason(),
                    headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else {
			// perform action after inserting/updating
			Object saveResult = this.afterSaveOrUpdate(entity);
			return new ResponseEntity<String>(
			        (saveResult == null ? null : JsonUtils.serialize(saveResult)),
			        headers, HttpStatus.OK);
		}
	}
	/**
	 * Perform action to insert/update entity into/from database.
	 * TODO children class should be override this method perform action
	 *
	 * @param entity the entity to insert/update entity into/from database
	 * @param create specify for creating new entity
	 *
	 * @return the effected records. (&lt; 0 for failed action)
	 */
	protected abstract int doSaveOrUpdate(T entity, boolean create);
	/**
	 * Perform action after inserting/updating entity.
	 * TODO children class should be override this method perform action
	 *
	 * @param entity the entity that has just been inserting/updating
	 *
	 * @return any result if neccessary to response to client. NULL for response empty
	 */
	protected Object afterSaveOrUpdate(T entity) { return null; }
	/**
	 * Check the specified entity whether existed
	 *
	 * @param entity the entity to check
	 *
	 * @return true if existing; else false
	 */
	protected abstract boolean existEntity(T entity);
	/**
	 * Validate the specified entity.
	 * TODO children class should be override this method to check entity before inserting/updating entity
	 *
	 * @param entity the entity to check
	 * @param create specify inserting/updating
	 *
	 * @return true if valid; else false
	 */
	protected boolean validateEntity(T entity, boolean create) { return true; }
    /**
     * Validate the specified entity.
     * TODO children class should be override this method to check entity before inserting/updating entity
     *
     * @param entity the entity to check
     * @param create specify inserting/updating
     *
     * @return response error entity if invalid; else null
     */
    protected ResponseEntity<String> validateResponseEntity(T entity, boolean create) { return null; }
    /**
     * Apply customize HTTP headers if necessary.<br>
     * TODO Children classes maybe override this method for customizing.<br>
     * Default is applying "content-type: application/json; charset=utf-8"
     *
     * @param headers to apply
     */
    protected void applyHttpHeaders(HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

	/**
	 * Delete the specified entity primary key
	 *
	 * @param entitypk the entity primary key parameter
     * @param result {@link BindingResult}
	 *
	 * @return the process status
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ExecutePermission(value = {
            FUNCTION_INSERTABLE_PERMISSION,
            FUNCTION_DELETABLE_PERMISSION,
            FUNCTION_WRITABLE_PERMISSION
    })
	public ResponseEntity<String> delete(@RequestBody(required = true) U entitypk, BindingResult result) {
		// check by bean validation
		this.checkBinding(result);

        // response headers
        HttpHeaders headers = new HttpHeaders();
        this.applyHttpHeaders(headers);

		// perform deletion
		try {
		    entitypk = Objects.requireNonNull(entitypk, "entitypk");
			// check entity whether existed
			if (this.findEntityBy(entitypk.getPk()) == null) {
				return new ResponseEntity<String>((String) null, headers, HttpStatus.NOT_FOUND);
			}
	        // check parameters if necessary
	        else if (!this.validateEntityDeletion(entitypk.getPk())) {
	            return new ResponseEntity<String>(
	                    super.getHttpStatusReason(HttpStatus.EXPECTATION_FAILED),
	                    headers, HttpStatus.EXPECTATION_FAILED);
	        }
	        // check parameters if necessary
	        else {
	            ResponseEntity<String> resp = this.validateResponseEntityDeletion(entitypk.getPk());
	            // if response status is error
	            if (resp != null
	                    && (resp.getStatusCodeValue() < HttpStatus.OK.value()
	                            || resp.getStatusCodeValue() >= HttpStatus.MULTIPLE_CHOICES.value())) {
	                // re-change status
	                String body = resp.getBody();
	                if (!StringUtils.hasText(body)) {
	                    body = super.getHttpStatusReason(HttpStatus.EXPECTATION_FAILED);
	                }
	                return new ResponseEntity<String>(body, headers, HttpStatus.EXPECTATION_FAILED);
	            }
	        }

			// perform deleting action
			return (!this.doDelete(entitypk.getPk())
					? new ResponseEntity<String>((String) null, headers, HttpStatus.NOT_FOUND)
							: new ResponseEntity<String>((String) null, headers, HttpStatus.OK));
		}
		catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>((String) null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	/**
	 * Delete the specified entity primary key
	 *
	 * @param entitypk the entity primary key parameter
     * @param result {@link BindingResult}
	 *
	 * @return the process status
	 */
	@RequestMapping(
			value = "/delete/v2",
			method = RequestMethod.POST,
			headers = "Accept=application/json",
			produces = "application/json"
	)
	@ExecutePermission(value = {
            FUNCTION_INSERTABLE_PERMISSION,
            FUNCTION_DELETABLE_PERMISSION,
            FUNCTION_WRITABLE_PERMISSION
    })
	public ResponseEntity<String> deleteModel(@ModelAttribute U entitypk, BindingResult result) {
		return this.delete(entitypk, result);
	}
    /**
     * Validate the specified entity whether should be deleted.
     * TODO children class should be override this method to check entity primary/unique key before deleting entity
     *
     * @param entitypk the entity primary/unique key to check
     *
     * @return true if valid; else false
     */
    protected boolean validateEntityDeletion(PK entitypk) { return true; }
    /**
     * Validate the specified entity whether should be deleted.
     * TODO children class should be override this method to check entity primary/unique key before deleting entity
     *
     * @param entitypk the entity primary/unique key to check
     *
     * @return response error entity if invalid; else null
     */
    protected ResponseEntity<String> validateResponseEntityDeletion(PK entitypk) { return null; }
	/**
	 * Perform action to delete entity from database.
	 * TODO children class should be override this method perform action
	 *
	 * @param entitypk the entity primary key parameter
	 *
	 * @return true for success; else false
	 */
	protected abstract boolean doDelete(PK entitypk);

	/**
	 * Upload files and data (require full permissions)
	 *
	 * @param data the uploaded data
	 * @param files uploaded files list
	 * @param request multi-part request to parse uploaded files (in files parameter empty case)
	 *
	 * @return the process status
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST
			, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@ExecutePermission(value = {
            FUNCTION_INSERTABLE_PERMISSION,
            FUNCTION_UPDATABLE_PERMISSION,
            FUNCTION_DELETABLE_PERMISSION,
            FUNCTION_WRITABLE_PERMISSION
    })
	public ResponseEntity<String> upload(
			@ModelAttribute(value = "data") final M data,
			@RequestParam(value = "files", required = true) final List<MultipartFile> files,
			final MultipartHttpServletRequest request) {

        // response headers
        HttpHeaders headers = new HttpHeaders();
        this.applyHttpHeaders(headers);

		// process uploaded data
		try {
		    if (CollectionUtils.isNotEmpty(files)) {
    			return this.processUploadData(new BaseUploadParamControllerDto<M>(files, data));
		    } else if (MapUtils.isNotEmpty(request.getFileMap())) {
		        return this.processUploadData(new BaseUploadParamControllerDto<M>(request, data));
		    } else {
		        return new ResponseEntity<String>((String) null, headers, HttpStatus.EXPECTATION_FAILED);
		    }
		}
		catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>((String) null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	/**
	 * Upload files and data (require full permissions)
	 *
	 * @param data the uploaded data
	 * @param files uploaded files list
	 * @param request multi-part request to parse uploaded files (in files parameter empty case)
	 *
	 * @return the process status
	 */
	@RequestMapping(value = "/upload/v2", method = RequestMethod.POST
			, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@ExecutePermission(value = {
            FUNCTION_INSERTABLE_PERMISSION,
            FUNCTION_UPDATABLE_PERMISSION,
            FUNCTION_DELETABLE_PERMISSION,
            FUNCTION_WRITABLE_PERMISSION
    })
	public ResponseEntity<String> uploadBody(
			@RequestBody final M data,
			@RequestParam(value = "files", required = true) final List<MultipartFile> files,
			final MultipartHttpServletRequest request) {
		return this.upload(data, files, request);
	}
	/**
	 * Upload files and data (require full permissions)
	 *
	 * @param data the uploaded data
	 * @param files uploaded files list
	 * @param request multi-part request to parse uploaded files (in files parameter empty case)
	 *
	 * @return the process status
	 */
	@RequestMapping(value = "/upload/v3", method = RequestMethod.POST
			, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@ExecutePermission(value = {
            FUNCTION_INSERTABLE_PERMISSION,
            FUNCTION_UPDATABLE_PERMISSION,
            FUNCTION_DELETABLE_PERMISSION,
            FUNCTION_WRITABLE_PERMISSION
    })
	public ResponseEntity<String> uploadParam(
			@RequestParam(name = "data") final M data,
			@RequestParam(value = "files", required = true) final List<MultipartFile> files,
			final MultipartHttpServletRequest request) {
		return this.upload(data, files, request);
	}
	/**
	 * Process uploaded data<br>
	 * TODO Children classes could override this method to receive and process uploaded data
	 *
	 * @param data to process
	 *
	 * @return the process status
	 */
	protected ResponseEntity<String> processUploadData(BaseUploadParamControllerDto<M> data) throws Exception {
		throw new IllegalAccessException("Not implement actions to process uploaded data!");
	}

	/**
	 * Download file with the specified parameters
	 *
	 * @param data the download parameters
	 * @param response to write data
     * @param result {@link BindingResult}
	 *
	 * @return the process status
	 */
	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public ResponseEntity<String> download(@ModelAttribute C data, HttpServletResponse response, BindingResult result) {
		// check by bean validation
		this.checkBinding(result);

		// response headers
        HttpHeaders headers = new HttpHeaders();
        this.applyHttpHeaders(headers);

		// process downloaded data
		Map<String, String> filePath = null;
		Map<String, File> file = null;
		Map<String, Workbook> workbook = null;
		Map<String, InputStream> stream = null;
		HttpStatus status = HttpStatus.NOT_FOUND;
		boolean implemented = false;
		try {
			// check download file path
		    if (!implemented) {
    			filePath = this.processDownloadPath(data);
    			if (MapUtils.isNotEmpty(filePath)) {
    				final Iterator<String> it = filePath.keySet().iterator();
    				final String downloadFileName = it.next();
    				final String downloadFilePath = filePath.get(downloadFileName);
    				status = DownloadUtils.downloadStatus(response, downloadFilePath, downloadFileName);
    				implemented = true;
    			}
		    }

			// check download file
		    if (!implemented) {
    			file = this.processDownloadFile(data);
    			if (MapUtils.isNotEmpty(file)) {
    				final Iterator<String> it = file.keySet().iterator();
    				final String downloadFileName = it.next();
    				final File downloadFile = file.get(downloadFileName);
    				status = DownloadUtils.downloadStatus(response, downloadFile.getPath(), downloadFileName);
    				implemented = true;
    			}
		    }

			// check download workbook
		    if (!implemented) {
    			workbook = this.processDownloadWorkbook(data);
    			if (MapUtils.isNotEmpty(workbook)) {
    				final Iterator<String> it = workbook.keySet().iterator();
    				final String downloadFileName = it.next();
    				final Workbook wrkbk = workbook.get(downloadFileName);
    				status = DownloadUtils.downloadStatus(response, wrkbk, downloadFileName);
    				implemented = true;
    			}
		    }

			// check download stream
		    if (!implemented) {
    			stream = this.processDownloadStream(data);
    			if (MapUtils.isNotEmpty(stream)) {
    				final Iterator<String> it = stream.keySet().iterator();
    				final String downloadFileName = it.next();
    				final InputStream is = stream.get(downloadFileName);
    				status = DownloadUtils.downloadStatus(response, is, downloadFileName);
    				implemented = true;
    			}
		    }

			// return not found
			if (!implemented) logger.debug("Not implement actions to process downloaded data!");
		}
		catch(Exception e) {
			logger.error(e.getMessage(), e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;

		} finally {
			if (MapUtils.isNotEmpty(file)) file.clear();
			if (MapUtils.isNotEmpty(workbook)) workbook.clear();
			if (MapUtils.isNotEmpty(stream)) stream.clear();
		}
		// response status
		response.setStatus(status.value());
		return new ResponseEntity<String>(
		        (status.value() == 200 ? null : super.getHttpStatusReason(status)),
		        headers, status);
	}
	/**
	 * Download file with the specified parameters
	 *
	 * @param data the download parameters
	 * @param response to write data
     * @param result {@link BindingResult}
	 *
	 * @return the process status
	 */
	@RequestMapping(value = "/download/v2", method = RequestMethod.POST)
	public ResponseEntity<String> downloadBody(@RequestBody C data, HttpServletResponse response, BindingResult result) {
		return this.download(data, response, result);
	}
	/**
	 * Process downloaded file and return the file path to download<br>
	 * TODO Children classes could override this method to process downloaded file
	 *
	 * @param data to process
	 *
	 * @return the map of download file name and file path to download
	 */
	protected Map<String, String> processDownloadPath(C data) throws Exception {
		return null;
	}
	/**
	 * Process downloaded {@link File} and return the {@link File} to download<br>
	 * TODO Children classes could override this method to process downloaded {@link File}
	 *
	 * @param data to process
	 *
	 * @return the map of download file name and {@link File} to download
	 */
	protected Map<String, File> processDownloadFile(C data) throws Exception {
		return null;
	}
	/**
	 * Process downloaded excel {@link Workbook} and return the {@link Workbook} to download<br>
	 * TODO Children classes could override this method to process downloaded {@link Workbook}
	 *
	 * @param data to process
	 *
	 * @return the map of download file name and {@link Workbook} to download
	 */
	protected Map<String, Workbook> processDownloadWorkbook(C data) throws Exception {
		return null;
	}
	/**
	 * Process downloaded {@link InputStream} and return the {@link InputStream} to download<br>
	 * TODO Children classes could override this method to process downloaded {@link InputStream}
	 *
	 * @param data to process
	 *
	 * @return the map of download file name and {@link InputStream} to download
	 */
	protected Map<String, InputStream> processDownloadStream(C data) throws Exception {
		return null;
	}
	
	/**
	 * Get the main entity type class
	 * 
	 * @return the main entity type class
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getMainEntityType() {
		return Optional.ofNullable(getClassGeneraicTypeByIndex(0))
				.map(ExceptionUtils.wrap(logger).function(Exceptions.wrap().function(t -> (Class<T>) t)))
				.filter(Optional::isPresent).map(Optional::get).orElse(null);
	}

	/**
	 * Get the entity search-condition type class
	 * 
	 * @return the entity search-condition type class
	 */
	@SuppressWarnings("unchecked")
	public Class<C> getSearchConditionType() {
		return Optional.ofNullable(getClassGeneraicTypeByIndex(1))
				.map(ExceptionUtils.wrap(logger).function(Exceptions.wrap().function(t -> (Class<C>) t)))
				.filter(Optional::isPresent).map(Optional::get).orElse(null);
	}
	
	/**
	 * Get the main entity primary unique key type class
	 * 
	 * @return the main entity primary unique key type class
	 */
	@SuppressWarnings("unchecked")
	public Class<PK> getUniqueKeyType() {
		return Optional.ofNullable(getClassGeneraicTypeByIndex(2))
				.map(ExceptionUtils.wrap(logger).function(Exceptions.wrap().function(t -> (Class<PK>) t)))
				.filter(Optional::isPresent).map(Optional::get).orElse(null);
	}
	
	/**
	 * Get the bound class of searching conditions type class
	 * 
	 * @return the bound class of searching conditions type class
	 */
	@SuppressWarnings("unchecked")
	public Class<S> getBoundSearchConditionType() {
		return Optional.ofNullable(getClassGeneraicTypeByIndex(3))
				.map(ExceptionUtils.wrap(logger).function(Exceptions.wrap().function(t -> (Class<S>) t)))
				.filter(Optional::isPresent).map(Optional::get).orElse(null);
	}
	
	/**
	 * Get the bound class of entity primary/unique key type class
	 * 
	 * @return the bound class of entity primary/unique key type class
	 */
	@SuppressWarnings("unchecked")
	public Class<U> getBoundUniqueKeyType() {
		return Optional.ofNullable(getClassGeneraicTypeByIndex(4))
				.map(ExceptionUtils.wrap(logger).function(Exceptions.wrap().function(t -> (Class<U>) t)))
				.filter(Optional::isPresent).map(Optional::get).orElse(null);
	}
	
	/**
	 * Get the attached upload data type class
	 * 
	 * @return the attached upload data type class
	 */
	@SuppressWarnings("unchecked")
	public Class<M> getAttachedUploadDataType() {
		return Optional.ofNullable(getClassGeneraicTypeByIndex(5))
				.map(ExceptionUtils.wrap(logger).function(Exceptions.wrap().function(t -> (Class<M>) t)))
				.filter(Optional::isPresent).map(Optional::get).orElse(null);
	}
}
