class CreateUsers < ActiveRecord::Migration[5.0]
  def change
    create_table :users do |t|
      t.string :nickname, null: false
      t.string :email, null: false
      t.string :device, null: true
      t.string :phone_number, null: false
      t.string :password_digest, null: false
      t.timestamps
    end
  end
end
