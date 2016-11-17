require 'gcm'
class NotificationManager
    @gcm_key = 'my key'
    def self.push_waipoint session_id, object
        users = User.where(:current_session_id => session_id).pluck('id')#_by_current_session_id(session_id).pluck('id')
        users
    end
end
