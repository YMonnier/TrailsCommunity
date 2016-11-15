require 'rails_helper'

RSpec.describe Api::UsersController, :type => :controller do
    describe 'POST #create' do
        context 'when is successfully created' do
            before(:each) do
                @user = FactoryGirl.build :user

                parameters = {
                    nickname: @user.nickname,
                    email: @user.email,
                    phone_number: @user.phone_number,
                    password: @user.password,
                    password_confirmation: @user.password
                }

                post :create, params: parameters
                @json = json_response
                @user_response = @json[:data]
            end

            it 'should be the same nickname' do
                expect(@user_response[:nickname]).to eql @user.nickname
            end

            it 'should be the same phone number' do
                expect(@user_response[:phone_number]).to eql @user.phone_number
            end

            it 'should be nil device value' do
                expect(@user_response[:device]).to eql nil
            end

            it { should respond_with 201 }
        end

        context 'when is failed without the same password confirmation' do
            before(:each) do
                @user = FactoryGirl.build :user

                parameters = {
                    nickname: @user.nickname,
                    email: @user.email,
                    phone_number: @user.phone_number,
                    password: @user.password,
                    password_confirmation: 'ERFERG'
                }

                post :create, params: parameters
                @json = json_response
                @user_response = @json[:data]
            end

            it { should respond_with 400 }
        end
    end
end
