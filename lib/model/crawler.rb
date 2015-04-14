require 'open-uri'
require 'time'

class Crawler
  include Mongoid::Document

  field :start_url, type: String
  field :started, type: Boolean, default: false
  field :start_time, type: Time, default: nil
  field :max_crawl_pages, type: Integer, default: 1

  has_and_belongs_to_many :pages, index: true

  index({ start_url: 1 }, { unique: true, name: "start_url_index" })

  # has_and_belongs_to_many :keywords

  def crawl!
    self.start_time = Time.now
    self.started = true
    self.save!

    urls = [self.start_url]


    next_urls = []

    while not urls.empty? do
      urls.each do |url|

        return if self.pages.count >= self.max_crawl_pages

        puts "url: #{url}"

        # handle failure case
        page = Page.find_by(url: url)

        if page.nil?
          puts url
          puts "url not in database. Retrieving from the web..."
          file = open(url)

          next if file.content_type != "text/html"

          body = file.read.force_encoding('UTF-8')
          page = self.pages.new(url: url, body: body)
          page.process_words!
        else
          page.crawlers.push(self)
          self.pages.push(page)
        end

        self.save!
        page.save!

        # parse response

        next_urls.concat page.next_urls
      end
      urls = next_urls
    end
  end
end