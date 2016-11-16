class CreateSessions < ActiveRecord::Migration[5.0]
  def change
    create_table :sessions do |t|
      t.integer :activity
      t.string :password, null: true
      t.string :departure_place
      t.string :arrival_place
      t.date :start_date
      t.boolean :close, default: false
      t.belongs_to :user, index: true, foreign_key: true
      t.timestamps
    end
  end
end
