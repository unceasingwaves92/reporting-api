package reportingapi.model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ApplicationVO {
    private long applicationId;
    private String description;
    private String applicationName;
    private String owner;
}