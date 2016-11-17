class Api::DevicesController < ApplicationController
    before_action :authenticate_user

    ##
    #
    # Add a device token to the current user
    # Header application/json
    # Header Authorization
    # {
    #   "device": 1
    # }
    #
    ##
    def create
        current_user.update_columns(device: params[:device])
        ok_request current_user
    end
end
