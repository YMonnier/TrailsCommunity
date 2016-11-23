class UserSerializer < ActiveModel::Serializer
  attributes :id, :nickname, :phone_number, :current_session_id, :password_digest
end
