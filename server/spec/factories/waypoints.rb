FactoryGirl.define do
    factory :waypoint do
        latitude { rand(43.000001...43.179363) }
        longitude { rand(5.000001...5.717782) }
        #user
        session
    end
end
