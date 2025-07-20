// guix shell openjdk openjdk:jdk
// java --enable-preview AtomFeed.java

/*
 * The perspective I'm trying to approach Java here is that class-based programming and OOP are not the same thing. Besides acting as a template of an object, a class in Java also acts as a container, a module of sorts.
 * 
 * Two kinds of objects:
 * 1. Data-based: can have methods that provide transformed views of the data they hold.
 * 2. Service-based: may have encapsulated state to provide service
 * 
 * The methods aim to provide deterministic return values.
 * The methods aim to act as messages. A caller of an object's method basically does not care about the data held by the callee object (encapsulation), but only cares about the messages that can be passed to the caller object.
 */

//JAVA 24
//PREVIEW
//DEPS com.fasterxml.jackson.core:jackson-core:2.19.1
//DEPS com.fasterxml.jackson.core:jackson-databind:2.19.1
//DEPS com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.1
//DEPS com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.17.1
//DEPS org.apache.commons:commons-text:1.13.1

import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.JsonProcessingException;

record Entry(
    String id,
    String title,
    String author_name,
    String content,
    String url,
    String excerpt,
    String[] collections,
    OffsetDateTime published,
    Optional<OffsetDateTime> updated) {
  public String serialize() {
    var updated = this.updated().orElse(this.published());

    var collections = Arrays
        .stream(this.collections())
        .map(c -> "<category term=\"%s\" label=\"%s\" />".formatted(c, c))
        .collect(Collectors.toList());

    return """
        <entry>
          <id>%s</id>
          <title type="html">%s</title>
          <updated>%s</updated>
          <author>
            <name>%s</name>
          </author>
          <content type="html">%s</content>
          <link href="%s"/>
          <summary type="html">%s</summary>
          %s
          <published>%s</published>
        </entry>
        """.formatted(
        this.id(),
        HtmlEncodingService.escape(this.title()),
        updated.format(DateTimeFormatter.ISO_DATE_TIME),
        this.author_name(),
        HtmlEncodingService.escape(this.content()),
        this.url(),
        HtmlEncodingService.escape(this.excerpt()),
        String.join("\n", collections),
        this.published().format(DateTimeFormatter.ISO_DATE_TIME));
  }
}

record JsonFeed(
    Entry[] entries) {
}

class HtmlEncodingService {
  public static String escape(String s) {
    // TODO: safe?
    // May be just use org.apache.commons.text.StringEscapeUtils?
    return s.replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#x27;");
  }
}

class JsonFeedService {
  public static JsonFeed parse(String s) throws JsonProcessingException {
    var mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.registerModule(new Jdk8Module());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    var entries = mapper.readValue(s, Entry[].class);
    return new JsonFeed(entries);
  }
}

class AtomFeedService {
  private String baseUrl;

  public AtomFeedService(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  private String feedId() {
    return baseUrl + "/";
  }

  private String feedUrl() {
    return baseUrl + "/feed.xml";
  }

  private OffsetDateTime latestModified(Entry[] entries) {
    return Arrays
        .stream(entries)
        .map(e -> e.updated().orElse(e.published()))
        .max(Comparator.naturalOrder())
        .orElseThrow();
  }

  public String generateAtomFeed(JsonFeed jf) {
    var entriesXml = Arrays
        .stream(jf.entries())
        .map(e -> e.serialize())
        .collect(Collectors.toList());

    var latestModified = latestModified(jf.entries());

    return """
        <?xml version="1.0" encoding="utf-8"?>
        <feed xmlns="http://www.w3.org/2005/Atom">
          <id>%s</id>
          <title>Jayesh Bhoot's Ghost Town – All Posts</title>
          <updated>%s</updated>
          <author>
            <name>Jayesh Bhoot</name>
          </author>
          <link href="%s" rel="self"/>
          %s
        </feed>
        """.formatted(
        feedId(),
        latestModified.format(DateTimeFormatter.ISO_DATE_TIME),
        feedUrl(),
        entriesXml);
  }

}

void main(String[] args) {
  try {
    var jsonStr = Files.readString(Path.of("index.json"));
    var jsonFeed = JsonFeedService.parse(jsonStr);
    var atomFeedService = new AtomFeedService("https://bhoot.dev");
    String atomFeed = atomFeedService.generateAtomFeed(jsonFeed);
    Files.writeString(
        Path.of("feed.xml"),
        atomFeed,
        StandardOpenOption.CREATE,
        StandardOpenOption.WRITE);
  } catch (JsonProcessingException e) {
    System.out.println("Failed while parsing JSON feed");
    e.printStackTrace();
  } catch (IOException e) {
    System.out.println("Failed while reading from JSON feed file or writing to Atom feed file");
    e.printStackTrace();
  }
}
