class Api::ChatsController < ApplicationController
    before_action :authenticate_user
    def create
        if params[:id]
            if Session.exists?(params[:id])
                @chat = Chat.new(chat_params)
                @chat.user_id = current_user.id
                @chat.session_id = params[:id]
                if @chat.save
                    created_request ''
                else
                    bad_request @chat.errors
                end
            else
                r = {session: 'Record Not Found'}
                return not_found r
            end
        end
    end

    def index
        if params[:id]
            if Session.exists?(params[:id])
                ok_request Chat.where(session_id: params[:id])
            else
                r = {session: 'Record Not Found'}
                return not_found r
            end
        else
            r = {session: 'session id is missing'}
            return bad_request r
        end
    end

    private
    def chat_params
        params.permit(:message)
    end
end
