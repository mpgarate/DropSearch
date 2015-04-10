require 'resque'

module CrawlJob
  @queue = :default

  def self.perform(params)
    puts "got here"
    sleep 1
    puts "Processed a job!"
  end
end