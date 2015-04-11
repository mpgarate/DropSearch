require 'resque'
require 'open-uri'
require 'mongo'

module ParseJob
  @queue = :parse

  def self.perform(page)

  end
end

module CrawlJob
  @queue = :crawl

  @client = Mongo::Client.new([ '127.0.0.1:27017' ], :database => 'drop_search')
  @pages_db = @client[:pages]

  def self.perform(url)
    urls = [url]

    1.times do 
      next_urls = []

      urls.each do |url|
        puts "--- - - - - - "
        page_file = open(url)
        next if page_file == nil
        body = page_file.read

        page = {}
        page['body'] = body

        @pages_db.insert_one(page)

        # todo: get urls from page to continue crawl
      end
    end


    file = open(url)
    puts file.read
  end
end
