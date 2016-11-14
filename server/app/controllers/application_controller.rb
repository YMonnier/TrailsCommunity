class ApplicationController < ActionController::API
    include ActionController::Serialization
    include Knock::Authenticable

    ##
    #
    # Return json request
    # with status 400/Bad Request.
    # Parameters object: Error message.
    #
    ##
    def bad_request object
      render json: { errors: object },
             status: :bad_request
    end

    ##
    #
    # Return json request
    # with status 404/Not Found.
    # Parameters object: Error message.
    #
    ##
    def not_found object
      render json: { errors: object },
             status: :not_found
    end

    ##
    #
    # Return json request
    # with status 201/Created.
    # Parameters object: Object created.
    #
    ##
    def created_request object
      render json: object,
             root: :data,
             status: :created
    end

    ##
    #
    # Return json request
    # with status 204/Deleted.
    # Parameters object: Object created.
    #
    ##
    def deleted_request
      r = {}
      render json: r,
             root: :data,
             status: :no_content
    end

    ##
    #
    # Return json request
    # with status 200/OK.
    # Parameters object: Object wanted.
    #
    ##
    def ok_request object, options=nil
      render json: object,
             root: :data,
             status: :ok,
             include: options
    end
end
