FactoryGirl.define do
    factory :session do
        activity { rand(1...10) }
        password { BCrypt::Password.create(FFaker::Internet.password) }
        departure_place { "#{rand(1...70)};#{rand(1...70)}" }
        arrival_place { "#{rand(1...70)};#{rand(1...70)}" }
        #Time.new.strftime('%Y-%m-%d')
        #start_date { FFaker::Time.date.to_s }
        start_date { Time.new.strftime('%Y-%m-%d') }
        close { false }
        user
    end
end
