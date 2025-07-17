// guix shell openjdk openjdk:jdk
// java --enable-preview AtomFeed.java

//DEPS com.fasterxml.jackson.core:jackson-core:2.19.1 
//DEPS com.fasterxml.jackson.core:jackson-databind:2.19.1 

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;

public record Person(String firstName, String lastName, int age) {}

void main(String[] args) {
  println("hello world!");
  var grant = new Person("Grant", "Hughes", 19);
  var mapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);

  try {
    var json = mapper.writeValueAsString(grant);
    System.out.println(json);
    var obj = mapper.readValue(json, Person.class);
    System.out.println(obj);
  } catch(JsonProcessingException e) {
    e.printStackTrace();
  }
}
