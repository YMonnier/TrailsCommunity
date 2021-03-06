class Waypoint < ApplicationRecord
    belongs_to :session
    validates :latitude,
              presence: true,
              allow_blank: false
    validates :longitude,
              presence: true,
              allow_blank: false
=begin
    validates :user_id,
              presence: true,
              allow_blank: false,
              numericality: true
=end
    validates :session_id,
              presence: true,
              allow_blank: false,
              numericality: true
end
