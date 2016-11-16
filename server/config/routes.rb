Rails.application.routes.draw do
    namespace :api, defaults: {format: :json} do
      mount Knock::Engine => '/login'
      resources :users, :only => [:create]

      # Sessions
      resources :sessions, :only => [:create, :update, :show, :index] do
          get :join, on: :member
      end
      #get 'sessions/:id/join', to: 'sessions#join'
    end
end
