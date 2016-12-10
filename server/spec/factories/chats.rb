FactoryGirl.define do
  factory :chat do
    message { FFaker::Lorem.sentence }
    user
    session
  end
end
