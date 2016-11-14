class UserSerializer < ActiveModel::Serializer
  attributes :id, :nickname, :phone_number
end
