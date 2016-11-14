Rails.application.routes.draw do
    namespace :api, defaults: {format: :json} do
      mount Knock::Engine => '/login'
      resources :users, :only => [:create]
    end
end
