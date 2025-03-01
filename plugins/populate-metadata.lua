function print_table(t)
    local i, v = next(t, nil)
    while i do
        print(v)
        i, v = next(t, i)
    end
end

function split(str, sep)
    local segments_i = 1
    local segments = {}
    local segment = ""
    local i = 1
    while (i <= strlen(str)) do
        local v = strsub(str, i, i)

        if v ~= sep then
            segment = segment .. v
        end

        if v == sep or i == strlen(str) then
            segments[segments_i] = segment
            segments_i = segments_i + 1
            segment = ""
        end

        i = i + 1
    end
    return segments
end

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

-- enrich article tag with: article ID, indieweb h-entry.
article_id_ele = HTML.select_one(page, "meta[itemprop='itemid']")
article_id = HTML.get_attribute(article_id_ele, "content")
article_ele = HTML.select_one(page, "main>article")
HTML.add_class(article_ele, "h-entry")
HTML.set_attribute(article_ele, "itemid", article_id)
HTML.set_attribute(article_ele, "itemscope", "")
HTML.set_attribute(article_ele, "itemtype", "https://schema.org/Article")
HTML.delete_element(article_id_ele)

-- indiewebify h1
local h1_ele = HTML.select_one(page, "h1")
if h1_ele ~= nil then
    HTML.add_class(h1_ele, "p-name")
end

-- add categories/tags above h1
-- Posted on <dt-published> in <coll1>,<coll2>
local published_container_ele = HTML.create_element("p", "Posted on ")

local published_meta_ele = HTML.select_one(page, "meta[itemprop='dt-published']")
local published_meta_value = HTML.get_attribute(published_meta_ele, "content")
local published_value_formatted = published_meta_value
if published_meta_value ~= "" then
    local tmp_file_name = tmpname()
    local date_cmd = format("date -d '%s' ", published_meta_value) .. " '+%-dXX %B %Y' | sed -e 's/11XX/11th/' -e 's/12XX/12th/' -e 's/13XX/13th/' -e 's/1XX/1st/' -e 's/2XX/2nd/' -e 's/3XX/3rd/' -e 's/XX/th/' " .. format(" > %s", tmp_file_name)
    execute(date_cmd)
    readfrom(tmp_file_name)
    published_value_formatted = read()
end
local published_ele = HTML.create_element("time", published_value_formatted)
HTML.add_class(published_ele, "dt-published")
HTML.set_attribute(published_ele, "datetime", published_meta_value)
local published_link_ele = HTML.create_element("a")
HTML.add_class(published_link_ele, "u-url")
HTML.set_attribute(published_link_ele, "href", page_url)
HTML.append_child(published_link_ele, published_ele)
HTML.append_child(published_container_ele, published_link_ele)

local category_container_ele = HTML.create_element("p", "Collected in ")
category_list_ele = HTML.select_one(page, "meta[itemprop='p-category']")
local category_list = HTML.get_attribute(category_list_ele, "content")
local categories = split(category_list, ",")
local has_category_mental_model = nil
local i, v = next(categories, nil)
while i do
    local category_ele = HTML.create_element("a", v)
    HTML.add_class(category_ele, "p-category")
    local collection_path = make_collection_path(v)
    HTML.set_attribute(category_ele, "href", format("/collections/%s", collection_path))
    HTML.set_attribute(category_ele, "rel", "tag")
    HTML.append_child(category_container_ele, category_ele)
    if v == "Mental model" then
        has_category_mental_model = 1
    end
    i, v = next(categories, i)
end

HTML.delete_element(published_meta_ele)
HTML.delete_element(category_list_ele)
if h1_ele ~= nil then
    local hgroup = HTML.create_element('hgroup')
    HTML.wrap(h1_ele, hgroup)
    HTML.append_child(hgroup, published_container_ele)
    HTML.append_child(hgroup, category_container_ele)
else
    HTML.prepend_child(article_ele, published_container_ele)
    HTML.prepend_child(article_ele, category_container_ele)
end

if has_category_mental_model ~= nil then
    local disclosure_template = [[
        <aside>
        <details>
        <summary>What's a mental model?</summary>
        <div>
        <p>My sole purpose is to describe how I visualise a concept in my mind when I think about it.</p>

        <p>The level of abstraction of a mental model is supposed to keep out the details of implementation, ensuring which I may fail at.</p>

        <p>A mental model is meant to be only a starting point for a concept. I create one for cases where an authoritative source is too dense to start with. Please refer to authoritative sources for deeper understanding.</p>

        <p>In other words, the aim is such that when you think about a concept, its mental model should put you into the right perspective. <i>Right, this is how something works, now let's check out the manual to see how I can put it to use.</i></p>
        </div>
        </details>
        </aside>
    ]]
    local disclosure_ele = HTML.parse(disclosure_template)
    local marker_ele = HTML.select_one(page, "hgroup:has(h1)")
    if marker_ele ~= nil then
        HTML.insert_after(marker_ele, disclosure_ele)
    else
        marker_ele = HTML.select_one(page, ".e-content")
        HTML.insert_before(marker_ele, disclosure_ele)
    end
end

-- -- wrap content in .e-content
-- Doesn't work well. Order of selected siblings is not maintained. Needs to be tested.
-- local content_wrapper = HTML.create_element("div")
-- HTML.add_class(content_wrapper, "e-content")
-- HTML.insert_after(h1_ele, content_wrapper)

-- local content_nodes = HTML.select(page, '.e-content ~ *')
-- local i, v = next(content_nodes, nil)
-- while i do
--     -- HTML.append_child(content_wrapper, v)
--     print(v)
--     i, v = next(content_nodes, i)
-- end
