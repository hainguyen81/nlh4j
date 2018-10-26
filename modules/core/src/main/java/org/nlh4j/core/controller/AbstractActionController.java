/**
 *
 */
package org.nlh4j.core.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.core.dto.BaseEntityParamControllerDto;
import org.nlh4j.core.dto.BaseSearchParamControllerDto;
import org.nlh4j.core.pagination.PaginationSearchDto;
import org.nlh4j.core.pagination.PagingDto;

/**
 * Abstract controller for master screens
 *
 * @param <T> the main entity type
 * @param <PK> the entity primary key type
 * @param <U> the bound class of entity primary/unique key
 * @param <M> the attached upload data type
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController
public abstract class AbstractActionController
    <T extends AbstractDto, PK extends AbstractDto, U extends BaseEntityParamControllerDto<PK>,
    M extends AbstractDto>
    extends AbstractMasterController
    <T, AbstractDto, PK, BaseSearchParamControllerDto<AbstractDto>, U, M> {

	/** */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.nlh4j.core.controller.AbstractMasterController#index()
	 */
	@Override
	@Deprecated
	public final ModelAndView index() {
	    throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see org.nlh4j.core.controller.AbstractMasterController#getIndexPage()
	 */
	@Override
	@Deprecated
	protected final String getIndexPage() {
	    throw new UnsupportedOperationException();
	}

	/* (Non-Javadoc)
	 * @see org.nlh4j.core.controller.AbstractMasterController#search(org.nlh4j.core.dto.BaseSearchParamControllerDto, org.springframework.validation.BindingResult)
	 */
	@Override
	@Deprecated
    public PagingDto<T> search(BaseSearchParamControllerDto<AbstractDto> searchPage, BindingResult result) {
	    throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.nlh4j.core.controller.AbstractMasterController#searchEntities(org.nlh4j.core.pagination.PaginationSearchDto)
	 */
	@Override
	@Deprecated
	protected final PaginationSearchDto<T, AbstractDto> searchEntities(PaginationSearchDto<T, AbstractDto> search) {
		throw new UnsupportedOperationException();
	}
}
