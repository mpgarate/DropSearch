require 'domainator'
require 'nokogiri'
require 'uri'

class Page
  include Mongoid::Document

  field :url, type: String
  field :body, type: String
  field :time, type: Time

  attr_reader :keywords

  has_and_belongs_to_many :crawlers

  def process_words!
    html = Nokogiri::HTML(self.body)
    text = html.at('body').inner_text
    text = text.encode!('UTF-8', 'UTF-8', :invalid => :replace)
    keywords = text.scan(/[a-z]+/i)


    keywords = keywords.map {|kw| Keyword.new(term: kw)}

    @keywords = keywords
  end

  def next_urls
    return [] if self.body.nil?

    html = Nokogiri::HTML(self.body)

    next_urls = html.css('a').map do |anchor|
      href = anchor['href']
      next if href.nil?
      begin
        URI.join(self.url, href)
      rescue URI::InvalidURIError
        nil
      end
    end

    # ignore empty hrefs
    next_urls = next_urls.compact

    # ignore mailto

    next_urls.delete_if do |url|
      url.to_s.starts_with? "mailto:"
    end

    # next_urls.delete_if do |url|
    #   url.nil? or url.to_s.empty?
    # end

    # ignore domains different from origin
    page_host = Domainator.parse(self.url)
    next_urls = next_urls.delete_if do |url|
      begin
        Domainator.parse(url) != page_host
      rescue Domainator::NotFoundError
        true
      end
    end

    # ignore anchor links to same page
    next_urls = next_urls.delete_if do |url|
      split_url = url.to_s.split("#")

      if split_url.count > 1 and split_url[0] == self.url
        true
      else
        false
      end
    end

    # ignore links to common media files
    next_urls = next_urls.delete_if do |url|
      ignored_types = Set.new(%w(wav wma ape flac doc docx jpg jpeg pages csv ppt aif iff
       mp3 mid flv swf png gif zip rar mp4 tar 7z jar gz exe rss xml m3u pdf bin
       ogg mov avi mp4))


      extname = File.extname(url.to_s)

      if extname.empty?
        false
      else
        extname = extname.downcase.squish.split(".")[1]
        ignored_types.include? extname
      end
    end

    next_urls.map {|url| url.to_s }
  end
end