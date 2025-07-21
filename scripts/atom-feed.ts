import fs from 'node:fs/promises';
import * as z from 'zod';
import { parseISO, isAfter } from 'date-fns';

const EntrySchema = z.object({
    id: z.string(),
    title: z.string(),
    author_name: z.string(),
    content: z.string(),
    url: z.string(),
    excerpt: z.string(),
    collections: z.array(z.string()),
    published: z.iso.datetime().transform(v => parseISO(v)),
    updated: z.nullable(z.iso.datetime().transform(v => parseISO(v)))
});

const JsonFeedSchema = z.array(EntrySchema);

type Entry = z.infer<typeof EntrySchema>;

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

    const updated = e.updated ?? e.published;

    return `<entry>
      <id>${e.id}</id>
      <title type="html">${escapeHtml(e.title)}</title>
      <updated>${updated.toISOString()}</updated>
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

const latestModified = (entries: readonly Entry[]): Date | undefined => {
    return entries.map(e => e.updated ?? e.published).reduce((acc, curr) => {
        if (!acc) return curr;
        return isAfter(curr, acc)
            ? curr
            : acc;
    }, undefined);
}

const makeFeed = (entries: readonly Entry[]) => {
    const latestModifiedDate = latestModified(entries);
    const latestModifiedTimeStamp = latestModifiedDate
        ? `<updated>${latestModifiedDate.toISOString()}</updated>`
        : '';

    const xmlEntries = entries
        .map(toXmlEntry)
        .join('\n');

    const feedId = baseUrl + "/";
    const feedUrl = baseUrl + "/feed.xml";

    return `<?xml version="1.0" encoding="utf-8"?>
    <feed xmlns="http://www.w3.org/2005/Atom">
      <id>${feedId}</id>
      <title>Jayesh Bhoot's Ghost Town – All Posts</title>
      ${latestModifiedTimeStamp}
      <author>
        <name>Jayesh Bhoot</name>
      </author>
      <link href="${feedUrl}" rel="self"/>
      ${xmlEntries}
    </feed>`;
}

const main = async () => {
    const jsonStr = await fs.readFile('index.json', { encoding: 'utf8' });
    const jsonFeed = JSON.parse(jsonStr);
    const entriesResult = JsonFeedSchema.safeParse(jsonFeed);
    if (entriesResult.success) {
        const entries = entriesResult.data;
        const atomFeed = makeFeed(entries);
        await fs.writeFile('site/feed.xml', atomFeed);
    } else {
        console.log(entriesResult.error);
    }
}