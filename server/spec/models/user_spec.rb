require 'rails_helper'

RSpec.describe User, :type => :model do
    before { @user = FactoryGirl.build(:user) }
    subject { @user }


    it { should respond_to :nickname }
    it { should respond_to :password }
    it { should have_secure_password }
    it { should respond_to :phone_number }
    it { should respond_to :device }

    it { should be_valid }

    it { should validate_presence_of :nickname }
    it { should validate_uniqueness_of :nickname }
    it { should validate_presence_of :phone_number }

    it { should_not allow_value('').for :nickname }

end
