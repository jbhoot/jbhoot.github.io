collection_links = HTML.select(page, config["selector"])
local i = 1
local count = size(collection_links)
while (i <= count) do
    collection_link = collection_links[i]
    if collection_link then
        collection_text = HTML.inner_text(collection_link)
        HTML.set_attribute(collection_link, "href", format("/collections/%s", collection_text))
    end
    i = i + 1
end
