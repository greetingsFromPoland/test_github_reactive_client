package pl.jarekwasowski.githubrclient.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Repository {

    private String ownerName;
    private String name;
    private boolean fork;

    @JsonProperty("owner")
    private void unpackNested(Map<String, Object> raw) {
        this.ownerName = (String) raw.get("login");
    }
}
