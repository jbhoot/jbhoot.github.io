//> using dep com.lihaoyi::os-lib:0.10.7
//> using dep com.lihaoyi::upickle:4.0.1
//> using dep com.lihaoyi::scalatags:0.13.1

import scalatags.Text.TypedTag
import ujson.Value
import scalatags.Text.all._
import java.time.Instant

var baseUrl = "https://bhoot.dev"

val exclude = List(
  "/about",
  "/accessibility-statement",
  "/about-website"
)

case class Entry(
    id: String,
    title: String,
    updated: Instant,
    author_name: String,
    content: String,
    url: String,
    excerpt: String,
    collections: List[String],
    published: Instant
)

val parseFromJson = (e: ujson.Value) => {
  val published = Instant.parse(e("published").str)
  val updated = e("updated").strOpt match
    case Some(updated) => Instant.parse(updated)
    case None          => published

  Entry(
    id = e("id").str,
    title = e("title").str,
    updated,
    author_name = e("author_name").str,
    content = e("content").str,
    url = baseUrl ++ e("url").str,
    excerpt = e("excerpt").str,
    collections = e("collections").arr.toList.map(v => v.str),
    published
  )
}

val toXmlEntry = (e: Entry) => {
  val collections =
    e.collections.map(t =>
      tag("category")(attr("term") := t, attr("label") := t)
    )
  tag("entry")(
    tag("id")(e.id),
    tag("title")(`type` := "html", e.title),
    tag("updated")(e.updated.toString()),
    tag("author")(tag("name")(e.author_name)),
    tag("content")(`type` := "html", e.content),
    link(href := e.url),
    tag("summary")(`type` := "html", e.excerpt),
    collections,
    tag("published")(e.published.toString())
  )
}

val makeFeed = (entries: List[Entry]) => {
  val latestModifiedEntry = entries.maxBy(e => e.updated)
  val xmlEntries = entries.map(toXmlEntry)
  val body = tag("feed")(
    xmlns := "http://www.w3.org/2005/Atom",
    tag("id")(baseUrl ++ "/"),
    tag("title")("Jayesh Bhoot's Ghost Town – All Posts"),
    tag("updated")(latestModifiedEntry.updated.toString()),
    tag("author")(tag("name")("Jayesh Bhoot")),
    link(href := baseUrl ++ "/feed.xml", rel := "self"),
    xmlEntries
  )
  """<?xml version="1.0" encoding="utf-8"?>""" ++ body.render
}

@main def main() =
  val json = ujson.read(os.read(os.pwd / "index.json"))

  val entries = json.arr.toList
    .filter(e => !exclude.contains(e("url").str))
    .map(parseFromJson)

  os.write.over(os.pwd / "site" / "feed.xml", makeFeed(entries))
