package com.baar.parent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ServerSentEventModel {

  String id;
  String event;
  Duration retry;
  String comment;
  String data;
}
