FactoryGirl.define do
  factory :user do
      password { FFaker::Internet.password(8) }
      email { FFaker::Internet.email }
      nickname { FFaker::Name.name }
      phone_number { FFaker::PhoneNumberFR.mobile_phone_number }
  end
end
