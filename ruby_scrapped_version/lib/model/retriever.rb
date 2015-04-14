class Retriever
  def self.search(start_url:, query:)
    results = Keyword.find_by(term: query)
      puts "results:"
      puts results
    []
  end
end