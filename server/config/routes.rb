Rails.application.routes.draw do
    namespace :api, defaults: {format: :json} do
      mount Knock::Engine => '/login'
      resources :users, :only => [:create] do
          get :me, on: :collection
      end
      resources :devices, :only => [:create]
      # Sessions
      resources :sessions, :only => [:create, :update, :show, :index] do
          get :join, on: :member
          post :waypoint, on: :member
          post :coordinate, on: :member
          get :test, on: :member
      end

      resources :chats, :only => [] do
        get :index, on: :member
        post :create, on: :member
      end
    end
end
