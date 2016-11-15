FactoryGirl.define do
  factory :session do
     activity { rand(1...10) }
     password { FFaker::Internet.password }
     departure_place { "#{rand(1...70)};#{rand(1...70)}" }
     arrival_place { "#{rand(1...70)};#{rand(1...70)}" }
     start_date { FFaker::Time.date.to_s }
  end
end
