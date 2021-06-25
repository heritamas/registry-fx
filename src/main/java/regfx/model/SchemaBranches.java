package regfx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hortonworks.registries.schemaregistry.SchemaBranch;

import java.util.List;

public class SchemaBranches {
    @JsonProperty
    public List<SchemaBranch> entities;
}
