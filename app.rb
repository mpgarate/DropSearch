require 'sinatra'
require 'resque'
require 'redis'
require './model/crawler'

require_relative './jobs'

Resque.redis = Redis.new

get '/' do
  "status: ok"
end

get '/start_crawl' do
  url = params['url']

  crawl_id = SecureRandom.uuid
  crawler = Crawler.new(url)
  crawler.save!
  Resque.enqueue(CrawlJob, crawler.id)

  # redirect to crawl overview page
  "started crawl"
end