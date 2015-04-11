require 'sinatra'
require 'open-uri'
require 'logger'
require 'resque'
require 'redis'
require 'mongo'
require 'json/ext'

require_relative './jobs'

logger = Logger.new(STDOUT)
logger.info("Program started")

Resque.redis = Redis.new

get '/' do
  "status: ok"
end

get '/start_crawl' do
  url = params['url']
  puts url
  Resque.enqueue(CrawlJob, url)

  "Handed off to Resque."
end