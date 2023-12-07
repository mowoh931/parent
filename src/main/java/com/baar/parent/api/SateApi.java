package com.baar.parent.api;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.baar.parent.model.State;
import com.baar.parent.service.StateService;
import com.baar.parent.utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/states")
public class SateApi {

  private final StateService stateService;

  public SateApi(StateService stateService) {
    this.stateService = stateService;
  }

  @GetMapping("/get/all")
  public List<State> states() throws IOException {
    List<State> states = stateService.states();
    ObjectMapper objectMapper = new ObjectMapper();
    String statesString = objectMapper.writeValueAsString(states);
    objectMapper.writeValue(new File(Constants.statesPath), statesString);
    return states;
  }

  @GetMapping(value = "/get/sse/", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public String statesSSE() throws IOException {
    String id = String.valueOf(new AtomicInteger(1).getAndIncrement());

    String state = stateService.states().stream().findAny().get().toString();
    String serverSentEvent =
        ServerSentEvent.builder(state)
                .id(id)

                .event("Sending stat").build().toString();
    return serverSentEvent;
  }

  @GetMapping(value = "/get/state", produces = MediaType.APPLICATION_JSON_VALUE)
  public State state() throws IOException {
    State state = stateService.states().stream().findAny().get();
    ObjectMapper objectMapper = new ObjectMapper();
    String output = objectMapper.writeValueAsString(state);
    objectMapper.writeValue(new File(Constants.statePathTxt), output);
    System.out.println(output);
    return state;
  }

  @GetMapping(value = "/get/sse/v2", produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<String> get() {
    ObjectMapper objectMapper = new ObjectMapper();

    String uri = "http://localhost:8085/api/states/get/sse/";
    RestTemplate restTemplate = new RestTemplate();
    String data = restTemplate.getForObject(uri, String.class);
    Stream<String> dataStream = Stream.of(data);
    Flux<String> dataStreamFlux =
        Flux.fromStream(dataStream)
            .checkpoint("My checkpoint in")
            .doOnNext(
                s -> {
                  System.out.println(s);
                  try {
                    String toFile = objectMapper.writeValueAsString(s);
                    objectMapper.writeValue(new File(Constants.sseStreamFluxPath), toFile);
                  } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                  } catch (IOException e) {
                    throw new RuntimeException(e);
                  }
                })
            .map(String::valueOf)
            .checkpoint("My checkpoint output");
    return dataStreamFlux;
  }
}
