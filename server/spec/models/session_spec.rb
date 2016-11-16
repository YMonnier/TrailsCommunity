require 'rails_helper'

RSpec.describe Session, :type => :model do
    before {
        @user = FactoryGirl.create(:user)
        @session = FactoryGirl.create(:session, user: @user) }
    subject { @session }


    it { should respond_to :activity }
    it { should respond_to :password }
    it { should respond_to :departure_place }
    it { should respond_to :arrival_place }
    it { should respond_to :start_date }
    it { should respond_to :user_id }

    it { should be_valid }

    it { should validate_presence_of :activity }
    it { should validate_presence_of :departure_place }
    it { should validate_presence_of :arrival_place }
    it { should validate_presence_of :start_date }
    it { should validate_presence_of :user_id }

    it { should_not allow_value('').for :activity }
    it { should_not allow_value('').for :departure_place }
    it { should_not allow_value('').for :arrival_place }
    it { should_not allow_value('').for :start_date }
    it { should_not allow_value('Number?').for :user_id }
    it { should allow_value(10).for :user_id }
    it { should_not allow_value('myNumber?').for :activity }
    it { should allow_value(10).for :activity }
end
