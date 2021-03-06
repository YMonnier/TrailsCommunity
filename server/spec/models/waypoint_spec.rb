require 'rails_helper'

RSpec.describe Waypoint, :type => :model do
    before {
        @user = FactoryGirl.create(:user)
        @session = FactoryGirl.create(:session, user: @user)
        @coords = FactoryGirl.create(:waypoint, session: @session)
    }

    subject { @coords }

    it { should respond_to :latitude }
    it { should respond_to :longitude }
    it { should respond_to :session_id }

    it { should be_valid }

    it { should validate_presence_of :latitude }
    it { should validate_presence_of :longitude }
    it { should validate_presence_of :session_id }

    it { should_not allow_value('myNumber?').for :session_id }
    it { should allow_value(10).for :session_id }
end
