class Api::UsersController < ApplicationController
    before_action :authenticate_user, :only => [:me]
    def create
        return bad_request 'password not the same' if params[:password] != params[:password_confirmation]

        @user = User.new(user_params)

        if @user.save
            return created_request @user
        else
            return bad_request @user.errors
        end
    end

    def me
        if params[:device_token]
            current_user.update_columns(device: params[:device_token])
        end
        ok_request current_user
    end

    private

    def user_params
        params.permit(:nickname, :email, :phone_number, :password)
    end
end
