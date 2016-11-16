class CreateCoordinates < ActiveRecord::Migration[5.0]
  def change
    create_table :coordinates do |t|
      t.float :latitude
      t.float :longitude
      t.belongs_to :session
      t.belongs_to :user
      t.timestamps
    end
  end
end
