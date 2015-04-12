require 'securerandom'
require 'resque'
require 'open-uri'
require './model/crawler'
require './model/page'

module CrawlJob
  @queue = :crawl

  def self.perform(crawler_id)
    crawler = Crawler.find(crawler_id)
    crawler.crawl!
  end
end
