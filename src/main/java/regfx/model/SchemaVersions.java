package regfx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hortonworks.registries.schemaregistry.SchemaVersionInfo;

import java.util.List;

public class SchemaVersions {
    @JsonProperty
    public List<SchemaVersionInfo> entities;
}
