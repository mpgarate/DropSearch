class Keyword

  attr_reader :term
  
  def initialize(term:)
    @term = term
  end

  def to_s
    self.term
  end

  def ==(other_object)
    return false if other_object.nil?
    return @term == other_object.term
  end

  def hash
    @term.hash
  end
end