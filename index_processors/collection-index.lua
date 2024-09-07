if not config["index_template"] then
  Plugin.fail("Please define the index_template option")
end

if not config["index_selector"] then
  Plugin.fail("Please define the index_selector option")
end

if not config["url_path"] then
  Plugin.fail("Please define the url_path option")
end
url_path = config["url_path"]

-- step 1: see pre-requisite and step 1 above
placeholder_container_on_main_index = HTML.select_one(page, config["index_selector"])
HTML.delete(placeholder_container_on_main_index)

-- Render entries on the blog page
env = {}
env["entries"] = site_index
posts = HTML.parse(String.render_template(config["index_template"], env))
container = HTML.select_one(page, config["index_selector"])
HTML.append_child(container, posts)

-- Create new pages for each collection

url_path = Sys.join_path(Sys.dirname(page_file), url_path)

-- Find all existing collections first
all_collections = {}
local i = 1
local count = size(site_index)
while (i <= count) do
  entry = site_index[i]
  local k = 1
  if not entry["collections"] then
    -- This entry has no collections, skip it
    i = i + 1
  else
    collection_count = size(entry["collections"])
    while (k <= collection_count) do
      all_collections[entry["collections"][k]] = 1
      k = k + 1
    end
  end
  i = i + 1
end

all_collections = Table.keys(all_collections)

function find_entries_with_collection(entries, collection)
  local es = {}
  local i = 1
  Log.debug("so far")
  local count = size(entries)
  local k = 1
  while (i <= count) do
    entry = entries[i]
    if not (Value.is_table(entry["collections"])) then
      -- No collections in this entry, so it definitely does not match
      i = i + 1
    else
      if Table.has_value(entry["collections"], collection) then
        es[k] = entry
        k = k + 1
      end
      i = i + 1
    end
  end
  return es
end

function build_collection_page(entries, collection)
  local matching_entries = find_entries_with_collection(entries, collection)
  local template = "<h1>Collection – {{collection}}</h1>" .. config["index_template"]
  local env = {}
  env["collection"] = collection
  env["entries"] = matching_entries
  posts = String.render_template(template, env)
  return posts
end

pages = {}

local i = 1
local collection_count = size(all_collections)
while (i <= collection_count) do
  collection = all_collections[i]
  Log.info(format("Generating a page for collection \"%s\"", collection))

  collection_page = {}
  collection_page["page_file"] = Sys.join_path(url_path, format("%s.html", collection))
  collection_page["page_content"] = build_collection_page(site_index, collection)

  pages[i] = collection_page

  i = i + 1
end

-- Finally, generate a page with a list of all collections
local collection_links = {}
local i = 1
local collection_count = size(all_collections)
while (i <= collection_count) do
  local collection = all_collections[i]
  local collection_link = {}
  collection_link["url"] = collection
  collection_link["title"] = collection
  collection_links[i] = collection_link

  i = i + 1
end

local template = [[
<h1>Collections</h1>
<ul>
{% for t in collection_links %}
  <li> <a href="{{t.url}}">{{t.title}}</a> </li>
{% endfor %}
</ul>
]]

local env = {}
env["collection_links"] = collection_links
local all_collections_page = {}
all_collections_page["page_file"] = Sys.join_path(url_path, "index.html")
all_collections_page["page_content"] = String.render_template(template, env)

pages[size(pages) + 1] = all_collections_page
