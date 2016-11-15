class User < ApplicationRecord
    has_secure_password

    validates :nickname,
              presence: true,
              uniqueness: true,
              allow_blank: false
    validates :email,
              presence: true,
              uniqueness: true,
              allow_blank: false,
              format: {with: /\A([^@\s]+)@((?:[-a-z0-9]+\.)+[a-z]{2,})\z/i}
    validates :password,
              length: { minimum: 8 },
              presence: true,
              allow_blank: false
    validates :phone_number,
              presence: true,
              allow_blank: false
end
