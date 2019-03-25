package template;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestTest {
    @Test
    void requestBuilder_should_build_expectedRequest() {
        Request request = Request.builder()
                .param("value")
                .build();
        assertThat(request).hasFieldOrPropertyWithValue("param", "value");
    }

    @Test
    void lombok_should_generates_expectedEqualsMethod() {
        Request first = Request.builder()
                .param("second")
                .build();
        Request second = Request.builder()
                .param("second")
                .build();
        assertThat(first).isNotSameAs(second);
        assertThat(first).isEqualTo(second);
    }
}