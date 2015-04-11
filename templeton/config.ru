require './templeton.rb'

run Rack::URLMap.new \
  "/"       => Sinatra::Application