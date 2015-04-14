require './inc/initialize'

module CrawlJob
  @queue = :crawl

  def self.perform(crawler_id)
    puts "crawler_id: #{crawler_id}"
    crawler = Crawler.find(crawler_id)
    crawler.crawl!
  end
end
