require 'rails_helper'

RSpec.describe Chat, :type => :model do
    before {
        @user = FactoryGirl.create(:user)
        @session = FactoryGirl.create(:session, user: @user)
        @chat = FactoryGirl.create(:chat, user: @user, session: @session)
    }

    subject { @chat }

    it { should respond_to :message }
    it { should respond_to :session_id }
    it { should respond_to :user_id }

    it { should be_valid }

    it { should validate_presence_of :message }
    it { should validate_presence_of :session_id }
    it { should validate_presence_of :user_id }

    it { should_not allow_value('Number?').for :user_id }
    it { should allow_value(10).for :user_id }
    it { should_not allow_value('myNumber?').for :session_id }
    it { should allow_value(10).for :session_id }
end
