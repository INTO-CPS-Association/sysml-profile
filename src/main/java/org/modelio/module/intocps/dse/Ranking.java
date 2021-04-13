
package org.modelio.module.intocps.dse;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "pareto"
})
public class Ranking {

    @JsonProperty("pareto")
    private Map<String, String> pareto = new HashMap<>();


    /**
     *
     * @return
     *     The pareto
     */
    @JsonProperty("pareto")
    public  Map<String, String> getPareto() {
        return this.pareto;
    }

    /**
     *
     * @param pareto
     *     The pareto
     */
    @JsonProperty("pareto")
    public void putPareto(String key, String direction) {
        this.pareto.put(key, direction);
    }

}
