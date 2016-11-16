class CoordinateSerializer < ActiveModel::Serializer
    belongs_to :user
    attributes :latitude, :longitude
end
