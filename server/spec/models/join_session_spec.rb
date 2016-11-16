require 'rails_helper'

RSpec.describe JoinSession, :type => :model do
  before {
      @user = FactoryGirl.create(:user)
      @session = FactoryGirl.create(:session, user: @user)
      @join = FactoryGirl.create(:join_session, user: @user, session: @session)
   }
  subject { @join }


  it { should respond_to :user_id }
  it { should respond_to :session_id }

  it { should be_valid }
end
