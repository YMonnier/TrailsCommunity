class SessionSerializer < ActiveModel::Serializer
    belongs_to :user
    has_many :coordinates
    has_many :waypoints
    has_many :chats
    attributes :id,
            :activity,
            :departure_place,
            :arrival_place,
            :start_date,
            :close,
            :lock,
            :created_at,
            :updated_at
end
