require 'open-uri'
require 'time'
require './model/page'

class Crawler
  include Mongoid::Document

  field :start_url, type: String
  field :started, type: Boolean, default: false
  field :start_time, type: Time, default: nil

  attr_reader :pages_visited


  def initialize(attrs = nil, options = nil)
    super
    @pages_visited = []
  end

  # has_many :pages
  # has_and_belongs_to_many :keywords

  def crawl!
    self.start_time = Time.now
    self.started = true
    save!

    urls = [self.start_url]

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

    @pages_visited << page
  end
end