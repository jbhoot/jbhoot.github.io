function make_collection_path(collection_text)
  -- collection_path = gsub(strlower(collection_text), " ", "-")
  local char_idx = 1
  local collection_path = ""
  while char_idx <= strlen(collection_text) do
    local currchar = strlower(strsub(collection_text, char_idx, char_idx))
    if currchar == " " then
      collection_path = collection_path .. "-"
    else
      collection_path = collection_path .. currchar
    end
    char_idx = char_idx + 1
  end
  return collection_path
end

local collection_links = HTML.select(page, config["selector"])
local i = 1
local count = size(collection_links)
while (i <= count) do
    local collection_link = collection_links[i]
    if collection_link then
        local collection_text = HTML.inner_text(collection_link)
        local collection_path = make_collection_path(collection_text)
        HTML.set_attribute(collection_link, "href", format("/collections/%s", collection_path))
    end
    i = i + 1
end
