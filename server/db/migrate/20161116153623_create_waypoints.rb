class CreateWaypoints < ActiveRecord::Migration[5.0]
  def change
    create_table :waypoints do |t|
      t.float :latitude
      t.float :longitude
      t.belongs_to :session
      t.timestamps
    end
  end
end
