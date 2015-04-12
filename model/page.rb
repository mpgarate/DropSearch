require 'nokogiri'
require 'mongo'

include Mongo

class Page
  #@@client = Mongo::Client.new([ '127.0.0.1:27017' ], :database => 'drop_search')
  #@@pages_db = @client[:pages]
  #@@keyword_index = @client[:keyword_index]

  include Mongoid::Document

  field :url, type: String
  field :body, type: Text

  def initialize(url:, start_url:, body:)
    @url = url
    @start_url = start_url
    @body = body
    @time = Time.now
    @uuid = SecureRandom.uuid
  end

  def process_words!
    html = Nokogiri::HTML(@body)
    text  = html.at('body').inner_text
    keywords = text.scan(/[a-z]+/i)
  end

  def save!
    @@pages_db.insert_one(self.to_h)
  end

  def to_h
    {
      url: @url,
      start_url: @start_url,
      body: @body,
      time: @time,
      uuid: @uuid
    }
  end
end