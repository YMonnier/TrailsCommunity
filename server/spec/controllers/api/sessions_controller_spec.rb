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

            it { expect(@data[:close]).to eql false }

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

        context 'when is no successfully getting because of authorization' do
            before do
                @session = FactoryGirl.create :session
                parameters = {
                    departure_place: @session.departure_place,
                    arrival_place: @session.arrival_place,
                    start_date: @session.start_date
                }
                post :create, params: parameters
            end

            it { should respond_with 401 }
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

        context 'when is no successfully getting because of authorization' do
            before do
                get :index
            end

            it { should respond_with 401 }
        end
    end

    describe 'GET #show' do
        context 'when is successfully got' do
            before do
                @user = FactoryGirl.create :user
                @session = FactoryGirl.create :session

                # Add Authorization
                token = generate_token @user
                api_authorization_header token

                parameters = {
                    id: @session.id
                }
                get :show, params: parameters

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

            it { should respond_with 200 }
        end

        context 'when is not successfully got - not found ' do
            before do
                @user = FactoryGirl.create :user
                @session = FactoryGirl.create :session

                # Add Authorization
                token = generate_token @user
                api_authorization_header token

                parameters = {
                    id: -1234
                }
                get :show, params: parameters

                @json = json_response
                @errors = @json[:errors]

            end



            it 'should be to have an error' do
                expect(@errors[:session]).to eql 'Record Not Found'
            end

            it { should respond_with 404 }
        end

        context 'when is no successfully getting because of authorization' do
            before do
                @session = FactoryGirl.create :session

                parameters = {
                    id: @session.id
                }
                get :show, params: parameters
            end

            it { should respond_with 401 }
        end
    end

    describe 'PUT #update' do
        before do
            @user = FactoryGirl.create :user
            @session = FactoryGirl.create :session
        end

        context 'when is successfully got' do
            before(:each) do
                # Add Authorization
                token = generate_token @user
                api_authorization_header token

                parameters = {
                    id: @session.id,
                    close: true
                }

                put :update, params: parameters

                @json = json_response
                @data = @json[:data]
            end

            it 'should be the same activity' do
                expect(@data[:id]).to eql @session.id
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

            it { should respond_with 200 }
        end

        context 'when is not successfully got - not found ' do
            before do
                # Add Authorization
                token = generate_token @user
                api_authorization_header token

                parameters = {
                    id: -1234
                }
                put :update, params: parameters

                @json = json_response
                @errors = @json[:errors]
            end

            it 'should be to have an error' do
                expect(@errors[:session]).to eql 'Record Not Found'
            end

            it { should respond_with 404 }
        end

        context 'when is no successfully getting because of authorization' do
            before do
                parameters = {
                    id: @session.id
                }
                put :update, params: parameters
            end

            it { should respond_with 401 }
        end
    end

    describe 'GET #join' do
        before do
            @user = FactoryGirl.create :user
            @session = FactoryGirl.create :session
        end

        context 'when is successfully got' do
            before(:each) do
                # Add Authorization
                token = generate_token @user
                api_authorization_header token

                parameters = {
                    id: @session.id
                }

                get :join, params: parameters
            end

            it { should respond_with 200 }
        end

        context 'when is not successfully got - not found ' do
            before do
                # Add Authorization
                token = generate_token @user
                api_authorization_header token

                parameters = {
                    id: -1234
                }
                get :join, params: parameters

                @json = json_response
                @errors = @json[:errors]
            end

            it 'should be to have an error' do
                expect(@errors[:session]).to eql 'Record Not Found'
            end

            it { should respond_with 404 }
        end

        context 'when is no successfully getting because of authorization' do
            before do
                parameters = {
                    id: @session.id
                }
                get :join, params: parameters
            end

            it { should respond_with 401 }
        end
    end
end
