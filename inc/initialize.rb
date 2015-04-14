require 'resque'
require 'redis'
require 'mongoid'
require './jobs'
require './lib/models'

Resque.redis = Redis.new

Mongoid.load!("./mongoid.yml", :development)

# add resque job to delete old crawls from database