package reportingapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationRequest {
    private long applicationId;
    private String description;
    private String applicationName;
    private String owner;

    @Override
    public String toString() {
        return "ApplicationRequest{" +
                "applicationId=" + applicationId +
                ", description='" + description + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}
