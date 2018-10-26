<#-- このテンプレートに対応するデータモデルのクラスは org.seasar.doma.extension.gen.DtoDesc です -->
<#import "/lib.ftl" as lib>
<#if lib.copyright??>
${lib.copyright}
</#if>
<#if packageName??>
package ${packageName};
</#if>

<#list lib.importDtoNames as importName>
import ${importName};
</#list>

/**
<#if showDbComment && comment??>
 * ${comment}
</#if>
<#if lib.author??>
 * @author ${lib.author}
</#if>
 *
 * @author (genarated by doma-gen)  $Author: $
 * @version $Revision:  $  $Date:  $
 */
@Data
public class ${simpleName}<#if superclassSimpleName??> extends ${superclassSimpleName}</#if> {
<#list ownEntityPropertyDescs as property>

  <#if showDbComment && property.comment??>
    /** ${property.comment} */
  <#else>
    /** */
  </#if>
  <#if property.version>
    @Version
  </#if>
    private ${property.propertyClassSimpleName} ${property.name};
</#list>
<#if originalStatesPropertyName??>

    /** */
    @OriginalStates
    private ${simpleName} ${originalStatesPropertyName};
</#if>

}