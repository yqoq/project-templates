package template;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Request {
    String param;
}
