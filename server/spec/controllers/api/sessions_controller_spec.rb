require 'rails_helper'

RSpec.describe Api::SessionsController, :type => :controller do
    describe 'POST #create' do
        context 'when is successfully created' do
            before do
                @user = FactoryGirl.create :user
                @session = FactoryGirl.create :session

                # Add Authorization
                token = generate_token @user
                api_authorization_header token
                parameters = {
                    activity: @session.activity,
                    password: @session.password,
                    departure_place: @session.departure_place,
                    arrival_place: @session.arrival_place,
                    start_date: @session.start_date
                }
                post :create, params: parameters

                @json = json_response
                @data = @json[:data]
            end

            it 'should be the same activity' do
                expect(@data[:activity]).to eql @session.activity
            end

            it 'should be the same departure place' do
                expect(@data[:departure_place]).to eql @session.departure_place
            end

            it 'should be the same arrival place' do
                expect(@data[:arrival_place]).to eql @session.arrival_place
            end

            it { should respond_with 201 }
        end

        context 'when is not successfully created' do
            before do
                @user = FactoryGirl.create :user
                @session = FactoryGirl.create :session

                # Add Authorization
                token = generate_token @user
                api_authorization_header token
                parameters = {
                    departure_place: @session.departure_place,
                    arrival_place: @session.arrival_place,
                    start_date: @session.start_date
                }
                post :create, params: parameters

                @json = json_response
                @data = @json[:data]
            end

            it { should respond_with 400 }
        end
    end

    describe 'GET #index' do
        context 'successfully' do
            before do
                @user = FactoryGirl.create :user
                @sessions = [FactoryGirl.create(:session), FactoryGirl.create(:session)]

                # Add Authorization
                token = generate_token @user
                api_authorization_header token

                get :index

                @json = json_response
                @data = @json[:data]
            end

            it 'should be the same size array' do
                expect(@data.size).to eql @sessions.size
            end

            it 'should be the same activity' do
                expect(@data[0][:activity]).to eql @sessions[0].activity
            end

            it 'should be the same departure place' do
                expect(@data[0][:departure_place]).to eql @sessions[0].departure_place
            end

            it 'should be the same arrival place' do
                expect(@data[0][:arrival_place]).to eql @sessions[0].arrival_place
            end

            it 'should be the same activity' do
                expect(@data[1][:activity]).to eql @sessions[1].activity
            end

            it 'should be the same departure place' do
                expect(@data[1][:departure_place]).to eql @sessions[1].departure_place
            end

            it 'should be the same arrival place' do
                expect(@data[1][:arrival_place]).to eql @sessions[1].arrival_place
            end

            it { should respond_with 200 }
        end
    end
end
