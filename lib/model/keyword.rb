class Keyword
  include Mongoid::Document
  field :term, type: String

  has_and_belongs_to_many :pages
  
  def initialize(term)
    @term = term
  end
end