
package org.modelio.module.intocps.traceability.type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * INTO-CPS Traceability JSON Schema Version 1.4
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "rdf:RDF"
})
public class Message {

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("rdf:RDF")
    private RdfRDF rdfRDF;

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("rdf:RDF")
    public RdfRDF getRdfRDF() {
        return this.rdfRDF;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("rdf:RDF")
    public void setRdfRDF(RdfRDF rdfRDF) {
        this.rdfRDF = rdfRDF;
    }

}
