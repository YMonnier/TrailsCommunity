class Coordinate < ApplicationRecord
    belongs_to :session
    belongs_to :user

    validates :latitude,
              presence: true,
              allow_blank: false
    validates :longitude,
              presence: true,
              allow_blank: false
    validates :user_id,
              presence: true,
              allow_blank: false,
              numericality: true
    validates :session_id,
              presence: true,
              allow_blank: false,
              numericality: true
end
