require 'fcm'
class NotificationManager
    @fcm_key = 'AAAAWvDhSUs:APA91bEQ8Dsl-ofG4s8wTtx_6UTmsLTA_e7Uc1M7s08-t-j2loCt2OePS3tK_4klDigGQ2fN_uWS0VPnNWWx_lseRBYl2FzHjga6gQeAQ7PZZ68CuATir2QFnvPeD-515-zBqQUYONUTJcSzWtPBBsjwF4WPEEoJUQ'
    @fcm = FCM.new(@fcm_key)

    def self.push_waipoint session_id, object
        users = User.where(:current_session_id => session_id).pluck('id')#_by_current_session_id(session_id).pluck('id')

        registration_ids= ['da-5V6tu638:APA91bEhK6Yue-hElQ1j8sHCLaf2bTWmgqUzyF-AqWOp034AFyoYd7TH6imCcAR8d0ftzqkNjNQRQJye5-QgyMEwCWkQ3UQKZ7Mu1Li7aWde6zrPh27YuROhcD5z1kA-r6LoVaZxTGOl'] # an array of one or more client registration tokens
        options = {data: {score: "123"}, collapse_key: "updated_score"}
        response = @fcm.send(registration_ids, options)

        response
    end
end
