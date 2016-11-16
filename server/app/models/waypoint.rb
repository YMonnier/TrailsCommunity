class Waypoint < ApplicationRecord
    belongs_to :session
    validates :latitude,
              presence: true,
              allow_blank: false
    validates :longitude,
              presence: true,
              allow_blank: false
end
