import fs from 'node:fs/promises';
import { Effect, Option, pipe, Schema } from "effect";
import { greaterThan, unsafeMake } from "effect/DateTime";

const EntrySchema = Schema.Struct({
  id: Schema.String,
  title: Schema.String,
  author_name: Schema.String,
  content: Schema.String,
  url: Schema.String,
  excerpt: Schema.String,
  collections: Schema.Array(Schema.String),
  published: Schema.DateFromString,
  updated: Schema.OptionFromNullOr(Schema.DateFromString)
});

const EntriesSchema = Schema.Array(EntrySchema);

type Entry = Schema.Schema.Type<typeof EntrySchema>

const decoder = Schema.decodeUnknown(EntriesSchema);

const baseUrl = "https://bhoot.dev";

const escapeHtml = (s: string) => {
  // Though the ">" character is escaped for symmetry, characters like ">" and "/" don't need escaping in HTML and have no special meaning unless they're part of a tag or unquoted attribute value. See Mathias Bynens's article (under "semi-related fun fact") for more details.
  const EscapeMap: Record<string, string> = {
    "&": "&amp;",
    "<": "&lt;",
    ">": "&gt;",
    '"': "&quot;",
    "'": "&#39;",
  };
  return s.replace(/[&<>"']/g, match => EscapeMap[match] || match);
}

const toXmlEntry = (e: Entry) => {
  const collections = e.collections
    .map(c => `<category term="${c}" label="${c}" />`)
    .join('\n');

  const updated = pipe(
    e.updated,
    Option.getOrElse(() => e.published),
    v => `<updated>${v.toISOString()}</updated>`
  );

  return `<entry>
    <id>${e.id}</id>
    <title type="html">${escapeHtml(e.title)}</title>
    ${updated}
    <author>
      <name>${e.author_name}</name>
    </author>
    <content type="html">${escapeHtml(e.content)}</content>
    <link href="${e.url}"/>
    <summary type="html">${escapeHtml(e.excerpt)}</summary>
    ${collections}
    <published>${e.published.toISOString()}</published>
  </entry>`;
}

const latestModified = (entries: readonly Entry[]) => {
  return entries
    .reduce((acc: Option.Option<Date>, curr) => {
      const currUpdated = Option.getOrElse(curr.updated, () => curr.published);
      const accUpdated = Option.getOrElse(acc, () => currUpdated);
      return greaterThan(unsafeMake(currUpdated), unsafeMake(accUpdated))
        ? Option.some(currUpdated)
        : Option.some(accUpdated);
    }, Option.none());
}

const makeFeed = (entries: readonly Entry[]) => {
  const latestModifiedTimeStamp =
    pipe(
      entries,
      latestModified,
      Option.map(v => `<updated>${v.toISOString()}</updated>`),
      Option.getOrElse(() => "")
    );

  const xmlEntries = entries
    .map(toXmlEntry)
    .join('\n');

  return `<?xml version="1.0" encoding="utf-8"?>
  <feed xmlns="http://www.w3.org/2005/Atom">
    <id>${baseUrl + "/"}</id>
    <title>Jayesh Bhoot's Ghost Town – All Posts</title>
    ${latestModifiedTimeStamp}
    <author>
      <name>Jayesh Bhoot</name>
    </author>
    <link href="${baseUrl + "/feed.xml"}" rel="self"/>
    ${xmlEntries}
  </feed>`;
}

const readFile = (file: string) =>
  Effect.tryPromise(() =>
    fs.readFile(file, { encoding: 'utf8' }));

const writeFile = (file: string) =>
  (feed: string) =>
    Effect.tryPromise(() =>
      fs.writeFile(file, feed))

const main = pipe(
  'index.json',
  readFile,
  Effect.map(JSON.parse),
  Effect.flatMap(decoder),
  Effect.map(makeFeed),
  Effect.map(writeFile('site/feed.xml')),
  Effect.flatten
);

Effect.runPromise(main);

// const program = pipe(
//   // read from input file
// Effect.andThen(FileSystem.FileSystem, fs => fs.readFileString("index.json", "utf8")),
// Effect.map(JSON.parse),
// Effect.flatMap(decoder),
// Effect.map(makeFeed),
// // Write to output file
// // TODO: How do I get the FileSystem context back here?
// // Effect.andThen(fs => fs.writeFile("site/feed.xml", "utf8")),
// );

// NodeRuntime.runMain(program.pipe(Effect.provide(NodeContext.layer)));
