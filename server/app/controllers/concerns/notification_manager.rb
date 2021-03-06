require 'fcm'
class NotificationManager
    @fcm_key = 'AAAAWvDhSUs:APA91bEQ8Dsl-ofG4s8wTtx_6UTmsLTA_e7Uc1M7s08-t-j2loCt2OePS3tK_4klDigGQ2fN_uWS0VPnNWWx_lseRBYl2FzHjga6gQeAQ7PZZ68CuATir2QFnvPeD-515-zBqQUYONUTJcSzWtPBBsjwF4WPEEoJUQ'
    @fcm = FCM.new(@fcm_key)

    ##
    #
    # Create a new push notification to share waypoint
    # location to other current session user
    #
    ##
    def self.push_waypoint current_user, object
        registration_users_ids = self.session_users current_user.current_session_id, current_user.id

        options = {data: self.waypoint_data(object), collapse_key: "waypoint"}
        @fcm.send(registration_users_ids, options)
    end

    ##
    #
    # Create a new push notification to share current
    # user coordinate to other current session user
    #
    ##
    def self.push_coordinate current_user, object
        registration_users_ids = self.session_users current_user.current_session_id, current_user.id

        options = {data: self.coordinate_data(object), collapse_key: "coordinate"}
        @fcm.send(registration_users_ids, options)
    end

    ##
    #
    # Create a new push notification to share chat message
    # to other current session user.
    #
    ##
    def self.push_chat_message current_user, object
        registration_users_ids = self.session_users current_user.current_session_id, current_user.id

        options = {data: self.chat_message_data(object), collapse_key: "chat_message"}
        @fcm.send(registration_users_ids, options)
    end

    def self.push_test current_user, object
        registration_users_ids = User.where(:current_session_id => current_user.current_session_id)
                                    .pluck('id')
                                    #.where.not(id: current_user.id)
        registration_users_ids = ['ddcIiS8yIFY:APA91bGc4pFN2oMrMEtyYkCo5IulpXhO8KWpvh8t47uk3R6rKAhKntD6ILCI2kaGgc5Nh7lnUEEFxPpVwksbZ74NQdwzV-6MiE4f8IvtjG0rMqrpxke9esdJ64aJdAgVVdkE0hIWi9z2']
        options = {data: self.chat_message_data(object), collapse_key: "test"}
        @fcm.send(registration_users_ids, options)
    end

    private
    def self.waypoint_data(object)
        {
            data: {
                type: 'waypoint',
                content: object
            }
        }
    end

    def self.coordinate_data(object)
        {
            data: {
                type: 'coordinate',
                content: object
            }
        }
    end

    def self.chat_message_data(object)
        {
            data: {
                type: 'chat_message',
                content: ActiveModelSerializers::SerializableResource.new(object, include: %w(user))
            }
        }
    end

    def self.session_users session_id, exclude_user
        User.where(:current_session_id => session_id)
            .where.not(id: exclude_user)
            .pluck('device')
    end
end
