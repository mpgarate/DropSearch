require './app.rb'

run Rack::URLMap.new \
  "/"       => Sinatra::Application