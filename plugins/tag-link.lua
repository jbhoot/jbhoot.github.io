tag_links = HTML.select(page, config["selector"])
local i = 1
local count = size(tag_links)
while (i <= count) do
    tag_link = tag_links[i]
    if tag_link then
        tag_text = HTML.inner_text(tag_link)
        HTML.set_attribute(tag_link, "href", format("/tags/%s", tag_text))
    end
    i = i + 1
end
