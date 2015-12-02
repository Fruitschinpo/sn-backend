(ns sn-backend.movie
  (:require [sn-backend.db :as db])
  (:gen-class))

(defn update-rating
  [body]
  (let [movie_id (:id body)
        rating (:value body)
        user_id (:user_id body)]
    (db/update-rating movie_id rating user_id)))