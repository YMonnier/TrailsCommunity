require 'rails_helper'

RSpec.describe Api::ChatsController, :type => :controller do
    describe 'POST #create' do
        context 'when is successfully created' do
            before do
                @user = FactoryGirl.create(:user)
                @session = FactoryGirl.create(:session, user: @user)
                @chat = FactoryGirl.create(:chat)

                # Add Authorization
                token = generate_token @user
                api_authorization_header token
                parameters = {
                    id: @session.id,
                    message: @chat.message
                }

                post :create, params: parameters
            end

            it { should respond_with 201 }
        end

        context 'when your are not authorized to create a chat message' do
            before do
                @user = FactoryGirl.create(:user)
                @session = FactoryGirl.create(:session, user: @user)
                @chat = FactoryGirl.create(:chat)

                parameters = {
                    id: @session.id,
                    message: @chat.message
                }

                post :create, params: parameters
            end

            it { should respond_with 401 }
        end
    end

    describe 'GET #index' do
        context 'when is successfully got' do
            before do
                @user = FactoryGirl.create(:user)
                @session = FactoryGirl.create(:session, user: @user)
                @chats = [FactoryGirl.create(:chat, user: @user, session: @session),
                    FactoryGirl.create(:chat, user: @user, session: @session)]

                # Add Authorization
                token = generate_token @user
                api_authorization_header token
                parameters = {
                    id: @session.id
                }

                get :index, params: parameters


                @json = json_response
                @data = @json[:data]
            end

            it 'should be the same size my_sessions array' do
                expect(@data.size).to eql @chats.size
            end

            it { should respond_with 200 }
        end

        context 'when your are not authorized to get all chat messages' do
            before do
                @user = FactoryGirl.create(:user)
                @session = FactoryGirl.create(:session, user: @user)
                @chat = FactoryGirl.create(:chat)

                parameters = {
                    id: @session.id
                }

                get :index, params: parameters
            end

            it { should respond_with 401 }
        end
    end
end
