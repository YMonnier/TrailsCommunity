require 'bcrypt'
class Api::SessionsController < ApplicationController
    before_action :authenticate_user

    ##
    #
    # Create a new session.
    # Header application/json
    # Header Authorization
    # {
    #   "activity": 1,
    #   "password": "", # optional
    #   "departure_place": "43.179363;5.717782",
    #   "arrival_place": "43.191168;5.730819",
    #   "start_date": "2016-11-20"
    # }
    #
    ##
    def create
        @session = Session.new(session_params)
        @session.user_id = current_user.id

        if params[:password]
            @session.password = BCrypt::Password.create(params[:password])
        end
        if @session.save
            created_request @session
        else
            bad_request @session.errors
        end
    end

    ##
    #
    # Get all session stored.
    # Return status 200.
    #
    ##
    def index
        ok_request Session.all
    end

    ##
    #
    # Get a specific session stored.
    # Request param: id
    # Return status 200 if session is found, otherwise,
    # status 404.
    #
    ##
    def show
        @session = Session.find params[:id]
        ok_request @session

    rescue ActiveRecord::RecordNotFound
        r = {session: 'Record Not Found'}
        return not_found r
    end

    def update
        @session = Session.find params[:id]

        if @session.update(session_params)
            ok_request @session
        else
            bad_request @session.errors
        end

    rescue ActiveRecord::RecordNotFound
        r = {session: 'Record Not Found'}
        return not_found r
    end

    ##
    #
    # Join the current session
    #
    ##
    def join
        id = params[:id]
        if Session.exists?(id)
            unless JoinSession.exists?(:user_id => current_user.id, :session_id => id)
                JoinSession.create(user_id: current_user.id, session_id: id)
            end
            current_user.update_columns(current_session_id: id)

            return ok_request ''
        else
            r = {session: 'Record Not Found'}
            return not_found r
        end
    end

    private
    def session_params
        params.permit(:activity,
        :departure_place,
        :arrival_place,
        :start_date)
    end
end
