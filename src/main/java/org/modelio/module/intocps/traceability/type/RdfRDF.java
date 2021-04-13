
package org.modelio.module.intocps.traceability.type;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "xmlns:rdf",
    "xmlns:prov",
    "messageFormatVersion",
    "prov:Agent",
    "prov:Entity",
    "prov:Activity"
})
public class RdfRDF {

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("xmlns:rdf")
    private RdfRDF.XmlnsRdf xmlnsRdf = RdfRDF.XmlnsRdf.fromValue("http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("xmlns:prov")
    private RdfRDF.XmlnsProv xmlnsProv = RdfRDF.XmlnsProv.fromValue("http://www.w3.org/ns/prov#");
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("messageFormatVersion")
    private RdfRDF.MessageFormatVersion messageFormatVersion = RdfRDF.MessageFormatVersion.fromValue("1.4");
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:Agent")
    private Set<ProvAgent> provAgent;
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:Entity")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<ProvEntity> provEntity = null;
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:Activity")
    private Set<ProvActivity> provActivity;

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("xmlns:rdf")
    public RdfRDF.XmlnsRdf getXmlnsRdf() {
        return this.xmlnsRdf;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("xmlns:rdf")
    public void setXmlnsRdf(RdfRDF.XmlnsRdf xmlnsRdf) {
        this.xmlnsRdf = xmlnsRdf;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("xmlns:prov")
    public RdfRDF.XmlnsProv getXmlnsProv() {
        return this.xmlnsProv;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("xmlns:prov")
    public void setXmlnsProv(RdfRDF.XmlnsProv xmlnsProv) {
        this.xmlnsProv = xmlnsProv;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("messageFormatVersion")
    public RdfRDF.MessageFormatVersion getMessageFormatVersion() {
        return this.messageFormatVersion;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("messageFormatVersion")
    public void setMessageFormatVersion(RdfRDF.MessageFormatVersion messageFormatVersion) {
        this.messageFormatVersion = messageFormatVersion;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:Agent")
    public Set<ProvAgent> getProvAgent() {
        return this.provAgent;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:Agent")
    public void setProvAgent(Set<ProvAgent> provAgent) {
        this.provAgent = provAgent;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:Entity")
    public Set<ProvEntity> getProvEntity() {
        return this.provEntity;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:Entity")
    public void setProvEntity(Set<ProvEntity> provEntity) {
        this.provEntity = provEntity;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:Activity")
    public Set<ProvActivity> getProvActivity() {
        return this.provActivity;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:Activity")
    public void setProvActivity(Set<ProvActivity> provActivity) {
        this.provActivity = provActivity;
    }

    public enum MessageFormatVersion {

        version("1.4");
        private final String value;
        private final static Map<String, RdfRDF.MessageFormatVersion> CONSTANTS = new HashMap<>();

        static {
            for (RdfRDF.MessageFormatVersion c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private MessageFormatVersion(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static RdfRDF.MessageFormatVersion fromValue(String value) {
            RdfRDF.MessageFormatVersion constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum XmlnsProv {

        HTTP_WWW_W_3_ORG_NS_PROV("http://www.w3.org/ns/prov#");
        private final String value;
        private final static Map<String, RdfRDF.XmlnsProv> CONSTANTS = new HashMap<>();

        static {
            for (RdfRDF.XmlnsProv c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private XmlnsProv(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static RdfRDF.XmlnsProv fromValue(String value) {
            RdfRDF.XmlnsProv constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum XmlnsRdf {

        HTTP_WWW_W_3_ORG_1999_02_22_RDF_SYNTAX_NS("http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        private final String value;
        private final static Map<String, RdfRDF.XmlnsRdf> CONSTANTS = new HashMap<>();

        static {
            for (RdfRDF.XmlnsRdf c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private XmlnsRdf(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static RdfRDF.XmlnsRdf fromValue(String value) {
            RdfRDF.XmlnsRdf constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
