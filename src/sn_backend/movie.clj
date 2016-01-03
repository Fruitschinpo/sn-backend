(ns sn-backend.movie
  (:require [sn-backend.db :as db]
            [clojure.set :refer :all])
  (:gen-class))

;; update rating for user
(defn update-rating
  [body]
  (let [movie_id (:movie body)
        rating (:rating body)
        user_id (:user_id body)]
    (if (db/has-user-rated-movie? movie_id user_id)
      (db/update-rating movie_id rating user_id)
      (db/insert-rating movie_id rating user_id))))

(defn all-genres
  []
  (db/get-all-genres))

(defn all-movies
  []
  (db/get-all-movies))
  
(defn get-user-rated-movies
  [body]
  (db/get-rated-movies (:user body)))

(defn get-movie
  [body]
  (let [movie_id (:movie body)]
    (if (contains? body :user)
      (db/get-movie-with-user-rating movie_id (:user body))
      (db/get-movie movie_id))))

(defn register-movie
  [body]
  (db/insert-movie (rename-keys body {:plot :description,
                                      :writers :writer,
                                      :directors :director,
                                      :mature :mature_rating_id})))
