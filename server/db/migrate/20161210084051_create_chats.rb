class CreateChats < ActiveRecord::Migration[5.0]
  def change
    create_table :chats do |t|
      t.string :message
      t.belongs_to :user
      t.belongs_to :session
      t.timestamps
    end
  end
end
