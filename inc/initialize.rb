require 'resque'
require 'redis'
require 'mongoid'

Resque.redis = Redis.new

Mongoid.load!("./mongoid.yml", :development)