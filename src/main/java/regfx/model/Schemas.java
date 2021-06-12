package regfx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hortonworks.registries.schemaregistry.SchemaMetadataInfo;

import java.util.List;

public class Schemas {
    @JsonProperty
    public List<SchemaMetadataInfo> entities;
}
