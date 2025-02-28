import fs from 'node:fs/promises';
import escape from 'lodash/escape.js';
import { isAfter } from 'date-fns';
import { Either, Option, Schema } from "effect"

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

type Entry = typeof EntrySchema.Type

const baseUrl = "https://bhoot.dev";

const exclude = [
  "/about",
  "/accessibility-statement",
  "/about-website"
];

const toXmlEntry = (e: Entry) => {
  const collections = e.collections
    .map(c => `<category term="${c}" label="${c}" />`)
    .join('\n');

  const updated = e.updated.pipe(
    Option.getOrElse(() => e.published),
    v => `<updated>${v.toISOString()}</updated>`);

  return `<entry>
    <id>${e.id}</id>
    <title type="html">${escape(e.title)}</title>
    ${updated}
    <author>
      <name>${e.author_name}</name>
    </author>
    <content type="html">${escape(e.content)}</content>
    <link href="${e.url}"/>
    <summary type="html">${escape(e.excerpt)}</summary>
    ${collections}
    <published>${e.published.toISOString()}</published>
  </entry>`;
}

const latestModified = (entries: Entry[]) => {
  return entries
    .reduce((acc: Option.Option<Date>, curr) => {
      const currUpdated = Option.getOrElse(curr.updated, () => curr.published);
      const accUpdated = Option.getOrElse(acc, () => currUpdated);
      return isAfter(currUpdated, accUpdated)
        ? Option.some(currUpdated)
        : Option.some(accUpdated);
    }, Option.none());
}

const makeFeed = (entries: Entry[]) => {
  const latestModifiedTimeStamp = latestModified(entries)
    .pipe(
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

const main = async () => {
  const jsonString = await fs.readFile('index.json', { encoding: 'utf8' });
  const json = JSON.parse(jsonString);

  const decode = Schema.decodeUnknownEither(EntriesSchema);
  const result = decode(json);

  if (Either.isRight(result)) {
    const feed = makeFeed(result.right.filter(e => !exclude.includes(e.url)));
    await fs.writeFile('site/feed.xml', feed);
  } else {
    console.error(result.left);
  }
}

await main();
