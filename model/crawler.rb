class Crawler
  include Mongoid::Document

  field :start_url, type: String

  def initialize(start_url)
    @start_url = start_url
    @started = false
  end

  def crawl!
    @start_time = Time.now

   urls = [start_url]

    while not urls.empty? do
      url = urls.shift      
      file = open(url)
      # handle failure case

      # parse response
      body = file.read
      page = Page.new(url: url, start_url: start_url, body: body)

      page.process_words!
      page.save!

      # urls.concat page.next_urls
    end 
  end
end