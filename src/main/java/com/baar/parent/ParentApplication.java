package com.baar.parent;

import com.baar.parent.model.ServerSentEventModel;
import com.baar.parent.model.State;
import com.baar.parent.utils.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParentApplication {

  public static void main(String[] args) throws IOException {
    SpringApplication.run(ParentApplication.class, args);
    System.out.println("**********************");

    ObjectMapper objectMapper = new ObjectMapper();

    String json = objectMapper.readValue(new File(Constants.statesPath), String.class);
    List jsonOutPut = objectMapper.readValue(json, List.class);
    System.out.println(jsonOutPut);
    System.out.println("**********************");

    jsonOutPut.forEach(
        e -> {
          System.out.println(e);
        });
    System.out.println("**********************");
    List<State> x = objectMapper.readValue(json, new TypeReference<List<State>>() {});
    x.forEach(
        state -> {
          System.out.println(state.getName() + ": " + state.getPopulation());
        });
    System.out.println("**********************");

    String jsonState = objectMapper.readValue(new File(Constants.statePathTxt), String.class);
    System.out.println(jsonState);
    System.out.println("**********************");
    State state = objectMapper.readValue(jsonState, State.class);
    System.out.println(state);
  }
}
