Rails.application.routes.draw do
    namespace :api, defaults: {format: :json} do
      mount Knock::Engine => '/login'
      resources :users, :only => [:create]
      resources :sessions, :only => [:create, :update, :show, :index]
    end
end
