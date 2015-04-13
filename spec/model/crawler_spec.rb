require 'spec_helper'
require './model/crawler'

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
    crawler = Crawler.new(start_url: "http://example.com/")

    crawler.crawl!

    expect(crawler.started).to be true

    expect(crawler.pages_visited[0].url).to eq "http://example.com/"
  end
end