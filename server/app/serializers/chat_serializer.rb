class ChatSerializer < ActiveModel::Serializer
  belongs_to :user
  attributes :id, :message, :user_id
end
