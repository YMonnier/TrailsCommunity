class Session < ApplicationRecord
    validates :activity,
              presence: true,
              allow_blank: false,
              numericality: true

    validates :departure_place,
              presence: true,
              allow_blank: false

    validates :arrival_place,
              presence: true,
              allow_blank: false
    validates :start_date,
              presence: true,
              allow_blank: false

end
