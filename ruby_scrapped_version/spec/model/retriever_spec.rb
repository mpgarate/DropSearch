require 'spec_helper'

describe Retriever do

  it "returns results for a search query" do

    start_url = "http://en.wikipedia.org/wiki/Albert_Gallatin"

    crawler = Crawler.find_or_create_by(
      start_url: start_url,
      max_crawl_pages: 15
      )

    crawler.crawl!

    results = Retriever.search(
      start_url: start_url,
      query: "New York University",
    )

    expect(results.count).to be > 1
  end
end