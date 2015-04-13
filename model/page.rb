require 'nokogiri'

class Page
  include Mongoid::Document

  field :url, type: String
  field :body, type: String
  field :time, type: Time

  has_one :crawler

  def process_words!
    html = Nokogiri::HTML(self.body)
    text = html.at('body').inner_text
    keywords = text.scan(/[a-z]+/i)
    # TODO: stemming words
  end
end