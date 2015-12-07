(ns sn-backend.movie
  (:require [sn-backend.db :as db])
  (:gen-class))

(defn update-rating
  [body]
  (let [movie_id (:movie body)
        rating (:rating body)
        user_id (:user_id body)]
	(if (> (db/update-rating movie_id rating user_id) 0)
	{}
        {:error "rating" :message "Could not update rating."})))

(defn all-genres
  []
  (db/get-all-genres))

(defn all-movies
  []
  (db/get-all-movies))
