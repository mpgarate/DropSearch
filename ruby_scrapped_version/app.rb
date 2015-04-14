require './inc/initialize'
require 'sinatra'
require './model/crawler'

get '/' do
  "status: ok"
end

get '/crawl/new' do
  url = params['url']

  return "FAILURE" if url == nil

  crawler = Crawler.new(start_url: url)
  crawler.save!

  Resque.enqueue(CrawlJob, crawler.id.to_s)

  # redirect "crawl/#{crawler.id}"
  "started crawl"
end