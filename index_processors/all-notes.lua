-- delete placeholder element that invoked this index script, as its not needed anymore
placeholder_container_on_main_index = HTML.select_one(page, config["index_selector"])
HTML.delete(placeholder_container_on_main_index)

-- retain only those entries marked with a Note category
local notes = {}
local i, entry = next(site_index, nil)
while i do
    local collections = entry["collections"]
    if collections then
        local j, coll = next(collections, nil)
        while j do
            if strlower(coll) == "note" then
                notes[i] = entry
            end
            j, coll = next(collections, j)
        end
    end
    i, entry = next(site_index, i)
end

-- step 2
env = {}
env["entries"] = notes
rendered_entries = HTML.parse(String.render_template(config["index_template"], env))

-- step 3
all_posts_index_file = Sys.join_path(Sys.dirname(page_file), "notes.html")
all_posts_index_content = HTML.pretty_print(rendered_entries)

-- step 4
pages = {}
pages[1] = {}
pages[1]["page_file"] = all_posts_index_file
pages[1]["page_content"] = all_posts_index_content
