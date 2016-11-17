class CoordinateSerializer < ActiveModel::Serializer
    belongs_to :user
    attributes :latitude, :longitude, :user_id
end
