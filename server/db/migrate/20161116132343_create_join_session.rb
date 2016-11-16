class CreateJoinSession < ActiveRecord::Migration[5.0]
    def change
        create_table :join_sessions, id: false do |t|
            t.belongs_to :user, index: true, foreign_key: true
            t.belongs_to :session, index: true, foreign_key: true
        end
    end
end
