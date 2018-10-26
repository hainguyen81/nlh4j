<#-- このテンプレートに対応するデータモデルのクラスは org.seasar.doma.extension.gen.DaoDesc です -->
 <#import "/lib.ftl" as lib>
<#if lib.copyright??>
${lib.copyright}
</#if>
<#if packageName??>
package ${packageName};
</#if>

<#list lib.importDaoNames as importName>
import ${importName};
</#list>
<#list lib.importNames as importName>
import ${importName};
</#list>
import java.util.List;

/**
 * {@link ${entityDesc.simpleName}} repository
 *
<#if lib.author??>
 * @author ${lib.author}
</#if>
 *
 * @author (genarated by doma-gen)  $Author: $
 * @version $Revision:  $  $Date:  $
 */
<#if configClassSimpleName??>@Dao(config = ${configClassSimpleName}.class)</#if>
<#if !configClassSimpleName??>@Dao</#if>
@InjectRepository
public interface ${simpleName} {

<#if entityDesc.tenantIdEntityPropertyDesc??>
    /**
     * Find companies by company code
     * @param companyCd to search
     * @return list of {@link ${entityDesc.simpleName}} entity
     */
    @Select
    List<${entityDesc.simpleName}> selectAll(String companyCd);
<#else>
    /**
     * Find all entities list
     * @return list of {@link ${entityDesc.simpleName}} entity
     */
    @Select
    List<${entityDesc.simpleName}> selectAll();
</#if>

<#if entityDesc.idEntityPropertyDescs?size gt 0>
    /**
     * Find entity by identity
<#list entityDesc.idEntityPropertyDescs as property>
     * @param ${property.name} to search
</#list>
     * @return the {@link ${entityDesc.simpleName}} entity
     */
    @Select
    ${entityDesc.simpleName} selectById(<#list entityDesc.idEntityPropertyDescs as property>${property.propertyClassSimpleName} ${property.name}<#if property_has_next>, </#if></#list>);

</#if>
<#if entityDesc.idEntityPropertyDescs?size gt 0 && entityDesc.versionEntityPropertyDesc??>
    /**
     * Find entity by identity and version
<#list entityDesc.idEntityPropertyDescs as property>
     * @param ${property.name} to search
</#list>
     * @param ${entityDesc.versionEntityPropertyDesc.name} version to filter
     * @return the {@link ${entityDesc.simpleName}} entity
     */
    @Select(ensureResult = true)
    ${entityDesc.simpleName} selectByIdAndVersion(<#list entityDesc.idEntityPropertyDescs as property>${property.propertyClassSimpleName} ${property.name}, </#list>${entityDesc.versionEntityPropertyDesc.propertyClassSimpleName} ${entityDesc.versionEntityPropertyDesc.name});

</#if>
    /**
     * Insert new entity
     * @param entity to insert
     * @return effected records
     */
    @Insert
    int insert(${entityDesc.simpleName} entity);
    /**
     * Batch inserting entities list
     * @param entities to insert
     * @return effected records
     */
    @BatchInsert
    int[] batchInsert(List<${entityDesc.simpleName}> entities);

    /**
     * Update the specified entity
     * @param entity to update
     * @return effected records
     */
    @Update
    int update(${entityDesc.simpleName} entity);
    /**
     * Batch updating the entities list
     * @param entities to update
     * @return effected records
     */
    @BatchUpdate
    int[] batchUpdate(List<${entityDesc.simpleName}> entities);

<#if entityDesc.logicalDeleteEntityPropertyDesc??>
    /**
     * @param entity
     * @return affected rows
     */
    @Update
    @LogicalDelete
    int deleteLogical(${entityDesc.simpleName} entity);
</#if>

    /**
     * Delete the specified entity
     * @param entity to delete
     * @return affected rows
     */
    @Delete
    int delete(${entityDesc.simpleName} entity);
    /**
     * Batch deleting the entities list
     * @param entities to delete
     * @return affected rows
     */
    @BatchDelete
    int[] batchDelete(List<${entityDesc.simpleName}> entities);
}