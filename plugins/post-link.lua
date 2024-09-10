post_link = HTML.select_one(page, config["selector"])
HTML.set_attribute(post_link, "href", page_url)