article_link = HTML.select_one(page, config["selector"])
HTML.set_attribute(article_link, "href", page_url)