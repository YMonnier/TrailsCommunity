class SessionSerializer < ActiveModel::Serializer
    belongs_to :user
    has_many :coordinates
    has_many :waypoints
  attributes :id, :activity, :departure_place, :arrival_place, :start_date, :close, :lock
end
