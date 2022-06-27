package pl.jarekwasowski.githubrclient.userinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionResponse {

    private String code;

    @JsonProperty("Message")
    private String message;
}
