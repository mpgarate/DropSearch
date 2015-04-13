require 'nokogiri'
require 'mongo'

include Mongo

class Page
  include Mongoid::Document

  field :url, type: String
  field :body, type: Text
  field :time, type: Time

  has_one :crawler

  def process_words!
    html = Nokogiri::HTML(@body)
    text  = html.at('body').inner_text
    keywords = text.scan(/[a-z]+/i)
  end
end