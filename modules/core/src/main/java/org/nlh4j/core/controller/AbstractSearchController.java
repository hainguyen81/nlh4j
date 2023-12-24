/**
 *
 */
package org.nlh4j.core.controller;

import java.util.Optional;

import com.machinezoo.noexception.Exceptions;

import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.core.dto.BaseEntityParamControllerDto;
import org.nlh4j.core.dto.BaseSearchParamControllerDto;
import org.nlh4j.util.ExceptionUtils;
import org.springframework.web.bind.annotation.RestController;

/**
 * Abstract controller for searching on master screens
 *
 * @param <T> the main entity type
 * @param <C> the entity search condition type
 * @param <S> the bound class of searching conditions
 * @param <M> the attached upload data type
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController
public abstract class AbstractSearchController
    <T extends AbstractDto, C extends AbstractDto, S extends BaseSearchParamControllerDto<C>,
    M extends AbstractDto>
    extends AbstractMasterController
    <T, C, AbstractDto, S, BaseEntityParamControllerDto<AbstractDto>, M> {

	/** */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * @see org.nlh4j.core.controller.MasterController#getDetailPage()
	 */
	@Override
	@Deprecated
	protected final String getDetailPage() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.nlh4j.core.controller.MasterController#findEntityBy(java.lang.Object)
	 */
	@Override
	@Deprecated
	protected final T findEntityBy(AbstractDto entity) {
		throw new UnsupportedOperationException();
	}
	/*
	 * (non-Javadoc)
	 * @see org.nlh4j.core.controller.MasterController#existEntityBy(java.lang.Object)
	 */
	@Override
	@Deprecated
	protected boolean isUnique(AbstractDto entitypk) {
	    throw new UnsupportedOperationException();
	}
	/*
	 * (non-Javadoc)
	 * @see org.nlh4j.core.controller.MasterController#existEntity(org.nlh4j.core.dto.AbstractDto)
	 */
	@Override
	@Deprecated
	protected final boolean existEntity(AbstractDto entity) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.nlh4j.core.controller.MasterController#doSaveOrUpdate(org.nlh4j.core.dto.AbstractDto, boolean)
	 */
	@Override
	@Deprecated
	protected final int doSaveOrUpdate(AbstractDto entity, boolean create) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.nlh4j.core.controller.MasterController#doDelete(java.lang.Object)
	 */
	@Override
	@Deprecated
	protected final boolean doDelete(AbstractDto entitypk) {
		throw new UnsupportedOperationException();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Class<S> getBoundSearchConditionType() {
		return Optional.ofNullable(getClassGeneraicTypeByIndex(2))
				.map(ExceptionUtils.wrap(logger).function(Exceptions.wrap().function(t -> (Class<S>) t)))
				.filter(Optional::isPresent).map(Optional::get).orElse(null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Class<M> getAttachedUploadDataType() {
		return Optional.ofNullable(getClassGeneraicTypeByIndex(3))
				.map(ExceptionUtils.wrap(logger).function(Exceptions.wrap().function(t -> (Class<M>) t)))
				.filter(Optional::isPresent).map(Optional::get).orElse(null);
	}
	
	@Override
	protected Class<AbstractDto> getUniqueKeyType() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected Class<BaseEntityParamControllerDto<AbstractDto>> getBoundUniqueKeyType() {
		throw new UnsupportedOperationException();
	}
}
