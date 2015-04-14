require 'spec_helper'

describe Crawler, "started" do
  it "is invalid without a start_url" do
    crawler = Crawler.new
    expect(false)
  end

  it "defaults to false" do
    crawler = Crawler.new(start_url: "http://example.com/")

    expect(crawler.started).to be false
  end

  it "crawls a page" do
    crawler = Crawler.find_or_create_by(start_url: "http://example.com/")

    crawler.crawl!

    expect(crawler.started).to be true

    expect(crawler.pages[0].url).to eq "http://example.com/"
  end

  it "crawls many pages" do
    crawler = Crawler.find_or_create_by(
      start_url: "http://en.wikipedia.org/wiki/Albert_Gallatin",
      max_crawl_pages: 15
      )

    crawler.crawl!

    expect(crawler.pages.count).to be > 5
  end
end