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
        active_sessions = []
        my_sessions = []
        history_sessions = []
        join_sessions = current_user.join_sessions
        Session.all.each { |session|
            if session.user_id == current_user.id
                my_sessions.push session_srz(session)
            elsif join_sessions.any? {|js| js.session_id == session.id} and session.close
                history_sessions.push session_srz(session)
            else
                active_sessions.push session_srz(session)
            end
        }


        json_object = {
            data: {
                active_sessions: active_sessions,
                my_sessions: my_sessions,
                history_sessions: history_sessions
                #history_sessions: current_user.join_sessions
            }
        }

        render json: json_object,
               status: :ok,
               include: %w(session.lock)#nil

        #ok_request render_json#, %w(user)
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
        ok_request @session, %w(user, coordinates, waypoints, chats.user)

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
            session = Session.find(id)
            if session.lock
                if params[:password]
                    if BCrypt::Password.new(session.password) != params[:password]
                        r = {session: 'Bad password.'}
                        return bad_request r
                    end
                else
                    r = {session: 'This session needs a password to join it.'}
                    return bad_request r
                end
            end

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

    ##
    #
    # Add a new waypoint to the current session.
    #
    # Header application/json
    # Header Authorization token
    # {
    #   "latitude": 43.179363,
    #   "longitude": 5.717782
    # }
    #
    ##
    def waypoint
        id = params[:id]
        if Session.exists?(id)
            @waypoint = Waypoint.new(coords_params)
            @waypoint.session_id = id
            if @waypoint.save
                NotificationManager::push_waypoint current_user, @waypoint
                return ok_request ''
            else
                return bad_request @waypoint.errors
            end
        else
            r = {session: 'Record Not Found'}
            return not_found r
        end
    end

    ##
    #
    # Add a new coordinates to the
    # current session linked by a user.
    #
    # Header application/json
    # Header Authorization token
    # {
    #   "latitude": 43.179363,
    #   "longitude": 5.717782
    # }
    #
    ##
    def coordinate
        id = params[:id]
        if Session.exists?(id)
            @coordinate = Coordinate.new(coords_params)
            @coordinate.session_id = id
            @coordinate.user_id = current_user.id
            if @coordinate.save
                NotificationManager::push_coordinate current_user, @coordinate
                return ok_request ''
            else
                return bad_request @coordinate.errors
            end
        else
            r = {session: 'Record Not Found'}
            return not_found r
        end
    end

    def test
        chat = Chat.new
        chat.message = 'Test my...'
        chat.user_id = current_user.id
        ok_request NotificationManager::push_test current_user, chat
    end

    private
    def session_params
        params.permit(:activity,
        :departure_place,
        :arrival_place,
        :start_date,
        :close)
    end

    def coords_params
        params.permit(:latitude, :longitude)
    end

    def session_srz session
        ActiveModelSerializers::SerializableResource.new(session, include: %w(session, session.lock))
    end
end
