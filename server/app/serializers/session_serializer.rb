class SessionSerializer < ActiveModel::Serializer
  attributes :id, :activity, :departure_place, :arrival_place, :start_date, :close
end
