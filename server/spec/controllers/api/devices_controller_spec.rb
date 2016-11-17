require 'rails_helper'

RSpec.describe Api::DevicesController, :type => :controller do
    describe 'POST #create' do
        context 'when is successfully created' do
            before do
                @user = FactoryGirl.create :user
                @device = '654C4DB3-3F68-4969-8ED2-80EA16B46EB0'

                # Add Authorization
                token = generate_token @user
                api_authorization_header token
                parameters = {
                    device: @device
                }

                post :create, params: parameters

                @json = json_response
                @data = @json[:data]
            end

            it { should respond_with 200 }
        end

        context 'when is no successfully getting because of authorization' do
            before do
                parameters = {
                    device: '654C4DB3-3F68-4969-8ED2-80EA16B46EB0'
                }
                post :create, params: parameters
            end

            it { should respond_with 401 }
        end
    end
end
