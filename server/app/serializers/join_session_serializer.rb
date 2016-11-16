class JoinSessionSerializer < ActiveModel::Serializer
    belongs_to :user
    belongs_to :session
end
