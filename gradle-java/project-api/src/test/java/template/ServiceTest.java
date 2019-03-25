package template;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceTest {
    @Mock
    private Service service;

    @Test
    void argument_matcher() {
        when(service.hello(
                argThat(p -> p.getParam().equals("request"))
        )).thenReturn(
                Response.builder().param("response").build()
        );
        Request request = Request.builder().param("request").build();
        Response response = service.hello(request);
        assertThat(response).hasFieldOrPropertyWithValue("param", "response");
    }

    @Test
    void catch_throwable() {
        when(service.hello(any(Request.class)))
                .thenThrow(new IllegalArgumentException());
        Request request = Request.builder().param("any").build();
        assertThatThrownBy(() ->
                service.hello(request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void await_response() {
        when(service.hello(any(Request.class)))
                .thenAnswer(i -> {
                    Thread.sleep(100);
                    throw new IllegalArgumentException();
                })
                .thenAnswer(i -> {
                    Thread.sleep(100);
                    return Response.builder().param("response").build();
                });
        Request request = Request.builder().param("request").build();
        given()
                .ignoreException(IllegalArgumentException.class)
                .await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        assertThat(service.hello(request)).hasFieldOrPropertyWithValue("param", "response")
                );
    }
}