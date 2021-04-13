
package org.modelio.module.intocps.traceability.type;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"prov:Entity"
})
public class ProvUsed {

	/**
	 *
	 * (Required)
	 *
	 */
	@JsonProperty("prov:Entity")
	@JsonDeserialize(as = java.util.LinkedHashSet.class)
	private Set<Id> provEntity = new HashSet<>();

	/**
	 *
	 * (Required)
	 *
	 */
	@JsonProperty("prov:Entity")
	public Set<Id> getProvEntity() {
		return this.provEntity;
	}


	/**
	 *
	 * (Required)
	 *
	 */
	@JsonProperty("prov:Entity")
	public void setProvEntityId(Set<Id> provEntity) {

		for (Id entity : provEntity){
			this.provEntity.add(entity);
		}
	}


	public void setProvEntity(Set<ProvEntity> provEntity) {

		for (ProvEntity entity : provEntity){
			this.provEntity.add(new Id (entity.getRdfAbout()));
		}
	}
//	/**
//	 *
//	 * (Required)
//	 *
//	 */
//	@JsonProperty("prov:Entity")
//	public void setProvEntity(Set<String> provEntity) {
//
//		for (String entity : provEntity){
////			if (entity instanceof ProvArtefact){
////				ProvArtefact temp = new ProvArtefact();
////				temp.setRdfAbout(((ProvArtefact) entity).getRdfAbout());
////				this.provEntity.add(temp);
////			}else{
////				ProvTool temp = new ProvTool();
////				temp.setRdfAbout(((ProvTool) entity).getRdfAbout());
////				this.provEntity.add(temp);
////			}
//			this.provEntity.add(entity);
//		}
//	}

//	public void addProvEntity(ProvArtefact provEntity) {
//
//		ProvArtefact temp = new ProvArtefact();
//		temp.setRdfAbout(((ProvArtefact) provEntity).getRdfAbout());
//		this.provEntity.add(provEntity);
//
//	}
//
//	public void addProvEntity(ProvTool provEntity) {
//
//		ProvTool temp = new ProvTool();
//		temp.setRdfAbout(((ProvTool) provEntity).getRdfAbout());
//		this.provEntity.add(provEntity);
//
//	}


	public void addProvEntity(String rdfAbout) {
		Id temp = new Id(rdfAbout);
		this.provEntity.add(temp);
	}

}
