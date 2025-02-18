local toc_ele = HTML.select_one(page, ".toc")
if toc_ele ~= nil then
    local heading_ele = HTML.create_element("h2", "Table of contents")
    HTML.append_child(toc_ele, heading_ele)
end
